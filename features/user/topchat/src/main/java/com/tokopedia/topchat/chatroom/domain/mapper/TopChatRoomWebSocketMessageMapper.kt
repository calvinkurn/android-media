package com.tokopedia.chatbot.domain.mapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUOTATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationAttributes
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerAttributesResponse
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import com.tokopedia.websocket.WebSocketResponse
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
class TopChatRoomWebSocketMessageMapper @Inject constructor(

) : WebsocketMessageMapper() {

    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_VOUCHER -> convertToVoucher(pojo, jsonAttributes)
            TYPE_QUOTATION -> convertToQuotation(pojo, jsonAttributes)
            TYPE_STICKER.toString() -> convertToSticker(pojo, jsonAttributes)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToSticker(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        val stickerAttributes = GsonBuilder().create().fromJson<StickerAttributesResponse>(jsonAttributes,
                StickerAttributesResponse::class.java)
        return StickerUiModel(
                messageId = pojo.msgId.toString(),
                fromUid = pojo.fromUid,
                from = pojo.from,
                fromRole = pojo.fromRole,
                attachmentId = pojo.attachment?.id.toString(),
                attachmentType = pojo.attachment?.type.toString(),
                replyTime = pojo.message.timeStampUnixNano,
                startTime = pojo.startTime,
                message = pojo.message.censoredReply,
                isRead = false,
                isDummy = false,
                isSender = !pojo.isOpposite,
                sticker = stickerAttributes.stickerProfile,
                source = pojo.source
        )
    }

    private fun convertToVoucher(item: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        val pojo = GsonBuilder().create().fromJson(jsonAttributes,
                TopChatVoucherPojo::class.java)
        val voucher = pojo.voucher
        var voucherType = MerchantVoucherType(voucher.voucherType, "")
        var voucherAmount = MerchantVoucherAmount(voucher.amountType, voucher.amount)
        var voucherOwner = MerchantVoucherOwner(
                identifier = voucher.identifier,
                ownerId = voucher.ownerId.toIntOrZero()
        )
        var voucherBanner = MerchantVoucherBanner(mobileUrl = voucher.mobileUrl)
        var voucherModel = MerchantVoucherModel(voucherId = voucher.voucherId.toIntOrZero(),
                voucherName = voucher.voucherName,
                voucherCode = voucher.voucherCode,
                merchantVoucherType = voucherType,
                merchantVoucherAmount = voucherAmount,
                minimumSpend = voucher.minimumSpend.toIntOrZero(),
                merchantVoucherOwner = voucherOwner,
                validThru = voucher.validThru.toString(),
                tnc = voucher.tnc,
                merchantVoucherBanner = voucherBanner,
                merchantVoucherStatus = MerchantVoucherStatus()
        )

        return TopChatVoucherUiModel(
                item.msgId.toString(),
                item.fromUid,
                item.from,
                item.fromRole,
                item.attachment?.id.toString(),
                item.attachment?.type.toString(),
                item.message.timeStampUnixNano,
                item.message.censoredReply,
                false,
                false,
                !item.isOpposite,
                voucherModel,
                "",
                item.blastId.toString(),
                item.source,
                voucher.isPublic
        )
    }

    private fun convertToQuotation(payload: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        val quotationAttributes = GsonBuilder()
                .create()
                .fromJson<QuotationAttributes>(jsonAttributes, QuotationAttributes::class.java)
        return QuotationUiModel(
                quotationPojo = quotationAttributes.quotation,
                messageId = payload.msgId.toString(),
                fromUid = payload.fromUid,
                from = payload.from,
                fromRole = payload.fromRole,
                attachmentId = payload.attachment?.id ?: "",
                attachmentType = payload.attachment?.type.toString(),
                replyTime = payload.message.timeStampUnixNano,
                isSender = !payload.isOpposite,
                message = payload.message.censoredReply,
                startTime = payload.startTime,
                source = payload.source
        )
    }

    fun parseResponse(response: WebSocketResponse?): ChatSocketPojo {
        return Gson().fromJson(response?.jsonObject, ChatSocketPojo::class.java)
    }

    fun mapToDummyMessage(
            messageId: String,
            userId: String,
            name: String,
            startTime: String,
            messageText: String
    ): Visitable<*> {
        return MessageViewModel(
                messageId, userId, name, startTime, messageText
        )
    }
}