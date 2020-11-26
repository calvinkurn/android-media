package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.unifycomponents.UnifyButton

class BroadcastSpamHandlerViewHolder(
        itemView: View?,
        private val listener: Listener?
) : AbstractViewHolder<BroadcastSpamHandlerUiModel>(itemView) {

    private val btnStopPromo: UnifyButton? = itemView?.findViewById(R.id.btn_stop_promo)
    private val btnFollowShop: UnifyButton? = itemView?.findViewById(R.id.btn_follow_shop)

    interface Listener {
        fun requestFollowShop(element: BroadcastSpamHandlerUiModel)
        fun requestBlockPromo(element: BroadcastSpamHandlerUiModel?)
    }

    override fun bind(element: BroadcastSpamHandlerUiModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_UPDATE_STATE) {
            bind(element)
        } else {
            super.bind(element, payloads)
        }
    }

    override fun bind(element: BroadcastSpamHandlerUiModel) {
        bindFollowShopButton(element)
        bindStopPromoButton(element)
        bindButtonEnableState(element)
        bindClickFollowShop(element)
        bindClickBlockPromo(element)
    }

    private fun bindButtonEnableState(element: BroadcastSpamHandlerUiModel) {
        if (element.isLoading) {
            disableAllButton()
        } else {
            enableAllButton()
        }
    }

    private fun bindFollowShopButton(element: BroadcastSpamHandlerUiModel) {
        btnFollowShop?.isLoading = element.isLoadingFollowShop
    }

    private fun bindStopPromoButton(element: BroadcastSpamHandlerUiModel) {
        btnStopPromo?.isLoading = element.isLoadingStopBroadCast
    }

    private fun bindClickBlockPromo(element: BroadcastSpamHandlerUiModel) {
        btnStopPromo?.setOnClickListener {
            element.startBlockPromo()
            startLoading(btnStopPromo)
            requestBlockPromo(element)
        }
    }

    private fun bindClickFollowShop(element: BroadcastSpamHandlerUiModel) {
        btnFollowShop?.setOnClickListener {
            element.startFollowShop()
            startLoading(btnFollowShop)
            requestFollowShop(element)
        }
    }

    private fun requestFollowShop(element: BroadcastSpamHandlerUiModel) {
        listener?.requestFollowShop(element)
    }

    private fun requestBlockPromo(element: BroadcastSpamHandlerUiModel) {
        listener?.requestBlockPromo(element)
    }

    private fun startLoading(button: UnifyButton?) {
        button?.isLoading = true
        disableAllButton()
    }

    private fun disableAllButton() {
        btnStopPromo?.isEnabled = false
        btnFollowShop?.isEnabled = false
    }

    private fun enableAllButton() {
        btnStopPromo?.isEnabled = true
        btnFollowShop?.isEnabled = true
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_spam_handler

        const val PAYLOAD_UPDATE_STATE = "update_state"
    }
}