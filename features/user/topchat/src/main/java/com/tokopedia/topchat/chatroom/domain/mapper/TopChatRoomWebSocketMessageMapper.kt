package com.tokopedia.chatbot.domain.mapper

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
class TopChatRoomWebSocketMessageMapper @Inject constructor() : WebsocketMessageMapper() {


    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_VOUCHER -> convertToVoucher(pojo, jsonAttributes)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToVoucher(item: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {

        val pojo = GsonBuilder().create().fromJson<TopChatVoucherPojo>(jsonAttributes,
                TopChatVoucherPojo::class.java)
        val voucher = pojo.voucher
        var voucherType = MerchantVoucherType(voucher.voucherType, "")
        var voucherAmount = MerchantVoucherAmount(voucher.amountType, voucher.amount)
        var voucherOwner = MerchantVoucherOwner(identifier = voucher.identifier, ownerId = voucher.ownerId)
        var voucherBanner = MerchantVoucherBanner(mobileUrl = voucher.mobileUrl)
        var voucherModel = MerchantVoucherModel(voucherId = voucher.voucherId,
                voucherName = voucher.voucherName,
                voucherCode = voucher.voucherCode,
                merchantVoucherType = voucherType,
                merchantVoucherAmount = voucherAmount,
                minimumSpend = voucher.minimumSpend,
                merchantVoucherOwner = voucherOwner,
                validThru = voucher.validThru.toString(),
                tnc = voucher.tnc,
                merchantVoucherBanner = voucherBanner,
                merchantVoucherStatus = MerchantVoucherStatus()
        )

        return TopChatVoucherViewModel(
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
                item.blastId.toString()
        )
    }
}