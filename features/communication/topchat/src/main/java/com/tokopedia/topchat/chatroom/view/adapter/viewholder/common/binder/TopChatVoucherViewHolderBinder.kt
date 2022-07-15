package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

object TopChatVoucherViewHolderBinder {

    const val SOURCE_MANUAL_ATTACHMENT= "attachment manual"
    const val SOURCE_BROADCAST = "broadcast"

    fun bindVoucherView(
        element: TopChatVoucherUiModel,
        merchantVoucherView: MerchantVoucherView?
    ) {
        merchantVoucherView?.onMerchantVoucherViewListener = object : MerchantVoucherView
        .OnMerchantVoucherViewListener {
            override fun onMerchantUseVoucherClicked(
                merchantVoucherViewModel: MerchantVoucherViewModel
            ) { }

            override fun isOwner(): Boolean {
                return element.isSender
            }
        }
        val merchantVoucherViewModel = element.voucher
        merchantVoucherView?.setData(merchantVoucherViewModel, false)
    }

    fun bindClick(
        element: TopChatVoucherUiModel,
        merchantVoucherView: MerchantVoucherView?,
        voucherListener: TopChatVoucherListener,
        source: String
    ) {
        merchantVoucherView?.setOnClickListener {
            voucherListener.onVoucherClicked(element, source)
        }
    }

}