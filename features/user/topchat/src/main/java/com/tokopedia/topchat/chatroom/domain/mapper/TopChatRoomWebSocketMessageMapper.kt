package com.tokopedia.chatbot.domain.mapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CTA_HEADER_MSG
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUOTATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.BaseChatUiModel.Builder.Companion.generateCurrentReplyTime
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationAttributes
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
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

    private val gson = GsonBuilder().create()

    override fun convertToMessageViewModel(pojo: ChatSocketPojo): Visitable<*> {
        return MessageUiModel.Builder()
            .withResponseFromWs(pojo)
            .build()
    }

    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_VOUCHER -> convertToVoucher(pojo, jsonAttributes)
            TYPE_QUOTATION -> convertToQuotation(pojo, jsonAttributes)
            TYPE_STICKER.toString() -> convertToSticker(pojo, jsonAttributes)
            TYPE_CTA_HEADER_MSG -> convertToCtaHeaderMsg(pojo, jsonAttributes)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToCtaHeaderMsg(
        pojo: ChatSocketPojo,
        jsonAttributes: JsonObject
    ): Visitable<*> {
        val attachment = gson.fromJson(
            jsonAttributes, HeaderCtaButtonAttachment::class.java
        )
        return MessageUiModel.Builder()
            .withResponseFromWs(pojo)
            .withAttachment(attachment)
            .build()
    }

    private fun convertToSticker(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        val stickerAttributes = GsonBuilder().create().fromJson(
            jsonAttributes, StickerAttributesResponse::class.java
        )
        return StickerUiModel.Builder()
            .withResponseFromWs(pojo)
            .withStickerProfile(stickerAttributes.stickerProfile)
            .build()
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

        return TopChatVoucherUiModel.Builder()
            .withResponseFromWs(item)
            .withVoucherModel(voucherModel)
            .withIsPublic(voucher.isPublic)
            .withIsLockToProduct(voucher.isLockToProduct?: 0)
            .withApplink(voucher.applink?: "")
            .build()
    }

    private fun convertToQuotation(
        payload: ChatSocketPojo, jsonAttributes: JsonObject
    ): Visitable<*> {
        val quotationAttributes = GsonBuilder()
                .create()
                .fromJson(jsonAttributes, QuotationAttributes::class.java)
        return QuotationUiModel.Builder()
            .withResponseFromWs(payload)
            .withQuotationPojo(quotationAttributes.quotation)
            .build()
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
        val localId = IdentifierUtil.generateLocalId()
        return MessageUiModel.Builder()
            .withMsgId(messageId)
            .withFromUid(userId)
            .withFrom(name)
            .withReplyTime(generateCurrentReplyTime())
            .withStartTime(startTime)
            .withMsg(messageText)
            .withLocalId(localId)
            .withIsDummy(true)
            .withIsSender(true)
            .withIsRead(false)
            .build()
    }
}