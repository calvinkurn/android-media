package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import okhttp3.Interceptor

class SendableVoucherPreview(
        val voucherPreview: VoucherPreview
) : SendablePreview {

    val voucherViewModel = MerchantVoucherModel(
            voucherPreview.voucherId,
            voucherPreview.voucherName,
            voucherPreview.voucherCode,
            MerchantVoucherType(voucherPreview.voucherType, voucherPreview.identifier),
            MerchantVoucherAmount(voucherPreview.amountType, voucherPreview.amount.toFloat()),
            voucherPreview.minimumSpend,
            MerchantVoucherOwner(),
            voucherPreview.validThru.toString(),
            voucherPreview.tnc,
            MerchantVoucherBanner(voucherPreview.desktopUrl, voucherPreview.mobileUrl),
            MerchantVoucherStatus()
    )

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun sendTo(messageId: String, opponentId: String, listInterceptor: List<Interceptor>) {

    }

    override fun notEnoughRequiredData(): Boolean {
        return false
    }

}