package com.tokopedia.topchat.chatroom.view.uimodel

import androidx.annotation.Keep
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER_ATTACHMENT
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.attachment.AttachmentId
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherAmount
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherBanner
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherOwner
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherStatus
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherType
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.WebsocketAttachmentContract
import com.tokopedia.topchat.chatroom.view.viewmodel.WebsocketAttachmentData

class SendableVoucherPreviewUiModel(
    private val voucherPreview: VoucherPreview
) : SendablePreview {

    private val voucher = MerchantVoucherModel(
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

    val merchantVoucherViewModel = MerchantVoucherViewModel(voucher).apply {
        this.isPublic = isPublic()
        this.isLockToProduct = isLockToProduct()
    }

    private fun isPublic(): Boolean {
        return voucherPreview.isPublic == 1
    }

    private fun isLockToProduct(): Boolean {
        return voucherPreview.isLockToProduct == 1
    }

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun generateMsgObj(
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String,
        sourceReply: String
    ): Any {
        val msgId = roomMetaData.msgId
        val toUid = roomMetaData.receiver.uid
        val voucherPayload = generatePayload(msgId, toUid, localId, sourceReply)
        return CommonUtil.toJson(voucherPayload)
    }

    private fun generatePayload(
        messageId: String,
        opponentId: String,
        localId: String,
        sourceReply: String
    ): WebsocketAttachmentContract {
        val startTime = SendableUiModel.generateStartTime()
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
            voucherPreview.isPublic,
            voucherPreview.isLockToProduct,
            voucherPreview.applink,
            voucherPreview.weblink
        )
        val data = WebsocketAttachmentData(
            message_id = messageId.toLongOrZero(),
            local_id = localId,
            message = getMessageFormat(),
            source = sourceReply,
            attachment_type = TYPE_VOUCHER_ATTACHMENT,
            start_time = startTime,
            payload = payload
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

    override fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel {
        return TopChatRoomVoucherUiModel.Builder()
            .withRoomMetaData(roomMetaData)
            .withAttachmentId(AttachmentId.NOT_YET_GENERATED)
            .withAttachmentType(AttachmentType.Companion.TYPE_VOUCHER)
            .withMerchantVoucherModel(voucher, voucherPreview.isLockToProduct, voucherPreview.isPublic)
            .build()
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
        val is_public: Int,
        val is_lock_to_product: Int,
        val applink: String,
        val weblink: String
    )
}
