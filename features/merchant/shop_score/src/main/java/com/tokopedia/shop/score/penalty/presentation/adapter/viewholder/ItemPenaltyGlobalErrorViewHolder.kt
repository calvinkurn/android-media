package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyGlobalErrorListener
import kotlinx.android.synthetic.main.item_shop_penalty_error_state.view.*

class ItemPenaltyGlobalErrorViewHolder(view: View, private val globalErrorListener: PenaltyGlobalErrorListener)
    : AbstractViewHolder<ErrorNetworkModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_penalty_error_state
    }

    override fun bind(element: ErrorNetworkModel?) {
        with(itemView) {
            globalErrorPenalty?.setType(GlobalError.SERVER_ERROR)
            globalErrorPenalty?.errorAction?.setOnClickListener {
                globalErrorListener.onBtnErrorClicked()
            }
        }
    }

}