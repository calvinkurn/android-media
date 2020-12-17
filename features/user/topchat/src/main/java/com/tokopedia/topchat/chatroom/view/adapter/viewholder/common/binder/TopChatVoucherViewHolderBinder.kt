package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.chatroom.view.listener.TopChatVoucherListener
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

object TopChatVoucherViewHolderBinder {

    fun bindVoucherView(
            element: TopChatVoucherUiModel,
            merchantVoucherView: MerchantVoucherView?,
            voucherListener: TopChatVoucherListener?
    ) {
        merchantVoucherView?.onMerchantVoucherViewListener = object : MerchantVoucherView.OnMerchantVoucherViewListener {
            override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
                voucherListener?.onVoucherCopyClicked(
                        merchantVoucherViewModel.voucherCode, element.messageId, element.replyId,
                        element.blastId, element.attachmentId, element.replyTime, element.fromUid
                )
            }

            override fun isOwner(): Boolean {
                return element.isSender
            }
        }
        merchantVoucherView?.setData(element.voucher, false)
    }

    fun bindClick(
            element: TopChatVoucherUiModel,
            merchantVoucherView: MerchantVoucherView?,
            voucherListener: TopChatVoucherListener
    ) {
        merchantVoucherView?.setOnClickListener {
            element.voucher.isPublic = !element.hasCtaCopy()
            voucherListener.onVoucherClicked(element.voucher)
        }
    }

}