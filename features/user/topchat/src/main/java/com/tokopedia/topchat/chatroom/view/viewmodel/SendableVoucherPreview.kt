package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.annotation.Keep
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER_ATTACHMENT
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import okhttp3.Interceptor

class SendableVoucherPreview(
        private val voucherPreview: VoucherPreview
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

    override fun generateMsgObj(
            messageId: String,
            opponentId: String,
            message: String,
            listInterceptor: List<Interceptor>
    ): Any {
        val voucherPayload = generatePayload(messageId, opponentId)
        return CommonUtil.toJson(voucherPayload)
    }

    private fun generatePayload(messageId: String, opponentId: String): WebsocketAttachmentContract {
        val startTime = SendableViewModel.generateStartTime()
        val payload = WebsocketVoucherPayload(
                voucherPreview.voucherId,
                voucherPreview.tnc,
                voucherPreview.voucherCode,
                voucherPreview.voucherName,
                voucherPreview.minimumSpend,
                voucherPreview.validThru,
                voucherPreview.desktopUrl,
                voucherPreview.mobileUrl,
                voucherPreview.amount,
                voucherPreview.amountType,
                voucherPreview.identifier,
                voucherPreview.voucherType,
                voucherPreview.isPublic
        )
        val data = WebsocketAttachmentData(
                messageId.toInt(),
                getMessageFormat(),
                "inbox",
                TYPE_VOUCHER_ATTACHMENT,
                startTime,
                payload
        )
        return WebsocketAttachmentContract(
                EVENT_TOPCHAT_REPLY_MESSAGE,
                data
        )
    }

    private fun getMessageFormat(): String {
        return "Voucher - ${voucherPreview.voucherName},\n" +
                "Minimum Pembelian: ${voucherPreview.minimumSpend},\n" +
                "Kode voucher: ${voucherPreview.voucherCode}"
    }

    override fun notEnoughRequiredData(): Boolean {
        return false
    }

    @Keep
    class WebsocketVoucherPayload(
            val voucher_id: Int,
            val tnc: String,
            val voucher_code: String,
            val voucher_name: String,
            val minimum_spend: Int,
            val valid_thru: Long,
            val desktop_url: String,
            val mobile_url: String,
            val amount: Int,
            val amount_type: Int,
            val identifier: String,
            val voucher_type: Int,
            val is_public: Int
    )
}