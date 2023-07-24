package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetBulkReviewMiniActionsBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewMiniActionsUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView

class WidgetBulkReviewMiniActions(
    context: Context,
    attrs: AttributeSet?
) : BaseReviewCustomView<WidgetBulkReviewMiniActionsBinding>(context, attrs) {
    override val binding = WidgetBulkReviewMiniActionsBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewMiniActionsUiState, animate: Boolean) {
        when (uiState) {
            is BulkReviewMiniActionsUiState.Hidden -> {
                animateHide(animate = animate, onAnimationEnd = { gone() })
            }
            is BulkReviewMiniActionsUiState.Showing -> {
                binding.bulkReviewMiniActionTestimony.updateUiState(uiState.miniActionTestimony)
                binding.bulkReviewMiniActionAttachment.updateUiState(uiState.miniActionAttachment)
                animateShow(animate = animate, onAnimationStart = { show() })
            }
        }
    }

    fun setListener(listener: Listener) {
        binding.bulkReviewMiniActionTestimony.setOnClickListener {
            listener.onClickTestimonyMiniAction()
        }
        binding.bulkReviewMiniActionAttachment.setOnClickListener {
            listener.onClickAddAttachmentMiniAction()
        }
    }

    interface Listener {
        fun onClickTestimonyMiniAction()
        fun onClickAddAttachmentMiniAction()
    }
}
