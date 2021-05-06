package com.tokopedia.internal_review.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.tokopedia.internal_review.R
import com.tokopedia.internal_review.analytics.ReviewTracking
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.internal_review.common.Const
import com.tokopedia.internal_review.common.InternalReviewUtils
import com.tokopedia.internal_review.factory.createReviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.sir_feedback_bottom_sheet.view.*
import java.net.UnknownHostException

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class FeedbackBottomSheet : BaseBottomSheet() {

    companion object {
        const val TAG = "SirFeedbackBottomSheet"
        private const val KEY_RATING = "key_rating"
        private const val PAGE_NAME = "popup feedback"

        fun createInstance(rating: Int): FeedbackBottomSheet {
            return FeedbackBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
                arguments = Bundle().apply {
                    putInt(KEY_RATING, rating)
                }
            }
        }
    }

    private val mViewModel by lazy { createReviewViewModel(this) }
    private var onSubmitted: (() -> Unit)? = null

    override fun getResLayout(): Int = R.layout.sir_feedback_bottom_sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeReviewState()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isSubmitted) {
            tracker.sendClickDismissBottomSheetEvent(PAGE_NAME)
        }
    }

    override fun setupView() = childView?.run {
        setOnTextAreaTextChanged()
        btnSirSubmitFeedback.setOnClickListener {
            setOnSubmitClicked()
        }
        btnSirCloseFeedback.setOnClickListener {
            dismiss()
        }
        imgSirFeedback.setImageUrl(Const.IMG_REQUEST_FEEDBACK)
    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setOnSubmittedListener(action: () -> Unit): FeedbackBottomSheet {
        onSubmitted = action
        return this
    }

    private fun setOnTextAreaTextChanged() = childView?.run {
        tauSirFeedback.textAreaInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                btnSirSubmitFeedback.isEnabled = !s.isNullOrBlank()
            }
        })
    }

    private fun observeReviewState() {
        mViewModel.reviewStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSubmitted?.invoke()
                    this.dismiss()
                }
                is Fail -> setOnError(it.throwable)
            }
        })
    }

    private fun setOnError(throwable: Throwable) {
        isSubmitted = false
        childView?.run {
            btnSirSubmitFeedback.isLoading = false
            showErrorToaster(throwable)
        }

        if (throwable is UnknownHostException) {
            tracker.sendImpressionNoNetworkEvent(PAGE_NAME)
        } else {
            tracker.sendImpressionErrorStateEvent(PAGE_NAME)
        }
    }

    private fun setOnSubmitClicked() = childView?.run {
        InternalReviewUtils.dismissSoftKeyboard(this@FeedbackBottomSheet)

        val isConnected = InternalReviewUtils.getConnectionStatus(requireContext())
        if (!isConnected) {
            setOnError(UnknownHostException())
            return@run
        }

        isSubmitted = true
        btnSirSubmitFeedback.isLoading = true
        val rating = arguments?.getInt(KEY_RATING).orZero()
        val feedback = tauSirFeedback.textAreaInput.text.toString()
        val param = getParams(rating, feedback)
        mViewModel.submitReview(param)
        tracker.sendImpressionLoadingStateEvent(PAGE_NAME)
    }
}