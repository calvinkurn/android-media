package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.databinding.WidgetReviewCredibilityGlobalErrorBinding
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityGlobalErrorUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ReviewCredibilityGlobalErrorWidget@JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseReviewCustomView<WidgetReviewCredibilityGlobalErrorBinding>(
    context, attributeSet, defStyleAttr
) {
    override val binding: WidgetReviewCredibilityGlobalErrorBinding =
        WidgetReviewCredibilityGlobalErrorBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private var listener: Listener? = null

    private fun hideWidget() {
        animateHide(onAnimationEnd = {
            listener?.onGlobalErrorTransitionEnd()
        })
    }

    private fun showData(data: ReviewCredibilityGlobalErrorUiModel) {
        showUIData(data)
        animateShow(onAnimationEnd = {
            listener?.onGlobalErrorTransitionEnd()
        })
    }

    private fun showUIData(data: ReviewCredibilityGlobalErrorUiModel) {
        val errorType = if (data.throwable is UnknownHostException || data.throwable is SocketTimeoutException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
        val errorMessage = ErrorHandler.getErrorMessage(context, data.throwable)
        binding.globalErrorReviewCredibility.setType(errorType)
        binding.globalErrorReviewCredibility.errorDescription.text = errorMessage
        binding.globalErrorReviewCredibility.setActionClickListener {
            listener?.onMainCTAClicked()
        }
    }

    fun updateUiState(uiState: ReviewCredibilityGlobalErrorUiState) {
        when (uiState) {
            is ReviewCredibilityGlobalErrorUiState.Hidden -> hideWidget()
            is ReviewCredibilityGlobalErrorUiState.Showed -> showData(uiState.data)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onMainCTAClicked()
        fun onGlobalErrorTransitionEnd()
    }
}