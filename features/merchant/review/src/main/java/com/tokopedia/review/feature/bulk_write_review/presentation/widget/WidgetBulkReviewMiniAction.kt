package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetBulkReviewMiniActionBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionUiState
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewMiniAction(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val binding = WidgetBulkReviewMiniActionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewMiniActionUiState) {
        when (uiState) {
            is BulkReviewMiniActionUiState.Hidden -> {
                gone()
            }
            is BulkReviewMiniActionUiState.Showing -> {
                binding.icBulkReviewMiniAction.setImage(uiState.iconUnifyId)
                binding.tvBulkReviewMiniAction.text = uiState.text.getStringValue(context)
                show()
            }
        }
    }

    fun setListener(listener: Listener) {
        binding.root.setOnClickListener {
            listener.onClickMiniAction()
        }
    }

    interface Listener {
        fun onClickMiniAction()
    }
}
