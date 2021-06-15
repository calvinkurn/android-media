package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTypeGlobalError
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyErrorListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyErrorUiModel
import kotlinx.android.synthetic.main.item_shop_penalty_error_state.view.*

class ItemPenaltyErrorViewHolder(view: View, private val itemPenaltyErrorListener: ItemPenaltyErrorListener): AbstractViewHolder<ItemPenaltyErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_penalty_error_state
    }

    override fun bind(element: ItemPenaltyErrorUiModel?) {
        with(itemView) {
            globalErrorPenalty?.setTypeGlobalError(element?.throwable)
            globalErrorPenalty?.errorAction?.setOnClickListener {
                itemPenaltyErrorListener.onRetryRefreshError()
            }
        }
    }

}