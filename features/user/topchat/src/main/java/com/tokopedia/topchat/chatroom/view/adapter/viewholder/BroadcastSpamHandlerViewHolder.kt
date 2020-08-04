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
    private var isLoading = false

    interface Listener {
        fun requestFollowShop(onSuccess: () -> Unit, onError: () -> Unit)
        fun onSuccessFollowShopFromBcHandler(bcHandlerPosition: Int)
    }

    override fun bind(element: BroadcastSpamHandlerUiModel) {
        bindState()
        bindClickFollowShop(element)
    }

    private fun bindState() {
        if (isLoading) {
            startLoading()
        } else {
            stopLoading()
        }
    }

    private fun bindClickFollowShop(element: BroadcastSpamHandlerUiModel) {
        btnFollowShop?.setOnClickListener {
            btnFollowShop.isLoading = true
            startLoading()
            requestFollowShop()
        }
    }

    private fun requestFollowShop() {
        listener?.requestFollowShop(::onSuccessFollowShop, ::onErrorFollowShop)
    }

    private fun onSuccessFollowShop() {
        btnFollowShop?.isLoading = false
        listener?.onSuccessFollowShopFromBcHandler(adapterPosition)
        stopLoading()
    }

    private fun onErrorFollowShop() {
        stopLoading()
    }

    private fun startLoading() {
        isLoading = true
        btnStopPromo?.isEnabled = false
        btnFollowShop?.isEnabled = false
    }

    private fun stopLoading() {
        isLoading = false
        btnStopPromo?.isEnabled = true
        btnFollowShop?.isEnabled = true
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_spam_handler
    }
}