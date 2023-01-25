package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetBulkReviewStickyButtonBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewStickyButton(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
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
                binding.btnBulkReviewSend.text = uiState.text.getStringValueWithDefaultParam(context)
                binding.cbBulkReviewAnonymous.setOnCheckedChangeListener(null)
                binding.cbBulkReviewAnonymous.isChecked = uiState.anonymous
                show()
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

    interface Listener {
        fun onAnonymousCheckChanged(checked: Boolean)
        fun onClickSubmitReview()
    }
}
