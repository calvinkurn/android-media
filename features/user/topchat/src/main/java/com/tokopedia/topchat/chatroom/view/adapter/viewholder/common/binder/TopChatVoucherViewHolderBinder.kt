package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

object TopChatVoucherViewHolderBinder {

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
        val merchantVoucherViewModel = element.voucher.apply {
            this.isPublic = element.isPublic()
        }
        merchantVoucherView?.setData(merchantVoucherViewModel,
            false, source = MerchantVoucherView.SOURCE_TOPCHAT)
    }

    fun bindClick(
        element: TopChatVoucherUiModel,
        merchantVoucherView: MerchantVoucherView?,
        voucherListener: TopChatVoucherListener
    ) {
        merchantVoucherView?.setOnClickListener {
            voucherListener.onVoucherClicked(element.voucher)
        }
    }

}