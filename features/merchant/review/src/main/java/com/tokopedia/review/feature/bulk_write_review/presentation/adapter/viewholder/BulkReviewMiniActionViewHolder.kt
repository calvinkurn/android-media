package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewMiniActionUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewMiniAction

class BulkReviewMiniActionViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<BulkReviewMiniActionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_bulk_review_mini_action
    }

    override fun bind(element: BulkReviewMiniActionUiModel) {
        (itemView as? WidgetBulkReviewMiniAction)?.run {
            updateUiState(element.uiState)
            setListener(object : WidgetBulkReviewMiniAction.Listener {
                override fun onClickMiniAction() {
                    listener.onClickMiniAction(element.uiState.iconUnifyId)
                }
            })
        }
    }

    interface Listener {
        fun onClickMiniAction(id: Int)
    }
}
