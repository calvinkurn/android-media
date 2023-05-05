package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetBulkReviewStickyButtonBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewStickyButton(
    context: Context,
    attrs: AttributeSet?
) : BaseCustomView(context, attrs) {
    private val binding = WidgetBulkReviewStickyButtonBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewStickyButtonUiState) {
        when (uiState) {
            is BulkReviewStickyButtonUiState.Hidden -> {
                gone()
            }
            is BulkReviewStickyButtonUiState.Showing -> {
                bind(uiState.text, uiState.anonymous, false)
            }
            is BulkReviewStickyButtonUiState.Submitting -> {
                bind(uiState.text, uiState.anonymous, true)
            }
        }
    }

    fun setListener(listener: Listener) {
        binding.cbBulkReviewAnonymous.setOnCheckedChangeListener { _, checked ->
            listener.onAnonymousCheckChanged(checked)
        }
        binding.btnBulkReviewSend.setOnClickListener {
            listener.onClickSubmitReview()
        }
    }

    private fun bind(buttonText: StringRes, isAnonymous: Boolean, submitting: Boolean) {
        binding.btnBulkReviewSend.text = buttonText.getStringValueWithDefaultParam(context)
        binding.cbBulkReviewAnonymous.setOnCheckedChangeListener(null)
        binding.cbBulkReviewAnonymous.isChecked = isAnonymous
        binding.btnBulkReviewSend.isLoading = submitting
        show()
    }

    interface Listener {
        fun onAnonymousCheckChanged(checked: Boolean)
        fun onClickSubmitReview()
    }
}
