package com.ruben.presentation.posture.detail

import android.os.Bundle
import android.view.View
import com.ruben.domain.base.Failure
import com.ruben.domain.postures.entity.PostureDetail
import com.ruben.presentation.R
import com.ruben.presentation.base.fragment.BaseFragment
import com.ruben.presentation.extensions.*
import kotlinx.android.synthetic.main.circular_progress_bar.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*
import kotlinx.android.synthetic.main.fragment_posture_detail.*
import kotlinx.android.synthetic.main.item_error.*

class PostureDetailFragment : BaseFragment() {

    companion object {
        fun getFragment(postureId: String): PostureDetailFragment =
            PostureDetailFragment().apply {
                setSerializableParam(postureId)
            }
    }

    override var fragmentLayout: Int = R.layout.fragment_posture_detail

    private lateinit var postureDetailViewModel: PostureDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewModel()
    }

    private fun setupListeners() {
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    private fun setupViewModel() {
        val postureId: String = getSerializableParam()
        postureDetailViewModel = viewModel(viewModelFactory.get()) {
            getPostureDetails(postureId)

            observe(ldPostureDetail, ::setupUI)

            observe(ldLoading, ::loadingUI)

            observe(ldFailure, ::handleFailure)

        }

    }

    private fun setupUI(postureDetail: PostureDetail) {
        errorLayout.gone()
        appBarLayout.visible()
        containerLayout.visible()
        with(postureDetail) {
            imageView.load(picture)
            collapsingToolbarLayout.title = name
            teacherTxv.text = getString(R.string.posture_detail_teacher_name, teacher)
            descriptionTxv.text = description
        }
    }

    private fun loadingUI(isLoading: Boolean) {
        if (isLoading) {
            appBarLayout.gone()
            containerLayout.gone()
            errorLayout.gone()
            progressBar.visible()
        } else {
            progressBar.gone()
        }
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is Failure.FailureWithMessage -> {
                appBarLayout.gone()
                containerLayout.gone()
                errorLayout.visible()
                errorMessage.text = failure.msg
                retryBtn.setOnClickListener { failure.retryAction() }
            }
        }
    }

}