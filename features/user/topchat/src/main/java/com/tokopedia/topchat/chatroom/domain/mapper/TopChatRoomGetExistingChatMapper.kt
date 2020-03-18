package com.tokopedia.topchat.chatroom.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_DUAL_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUOTATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.ImageDualAnnouncementPojo
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationAttributes
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import javax.inject.Inject

/**
 * @author : Steven 02/01/19
 */

open class TopChatRoomGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_IMAGE_DUAL_ANNOUNCEMENT -> convertToDualAnnouncement(chatItemPojoByDateByTime)
            TYPE_VOUCHER -> convertToVoucher(chatItemPojoByDateByTime)
            TYPE_QUOTATION -> convertToQuotation(chatItemPojoByDateByTime)
            else -> super.mapAttachment(chatItemPojoByDateByTime)
        }
    }

    private fun convertToVoucher(item: Reply): Visitable<*> {
        var temp = item.attachment?.attributes

        val pojo = GsonBuilder().create().fromJson<TopChatVoucherPojo>(MethodChecker.fromHtml(temp).toString(),
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

        return TopChatVoucherUiModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id ?: "",
                item.attachment?.type.toString(),
                item.replyTime,
                item.msg,
                item.isRead,
                false,
                !item.isOpposite,
                voucherModel,
                item.replyId.toString(),
                item.blastId.toString()
        )
    }

    private fun convertToDualAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageDualAnnouncementPojo>(item.attachment?.attributes,
                ImageDualAnnouncementPojo::class.java)
        return ImageDualAnnouncementUiModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id ?: "",
                item.attachment?.type.toString(),
                item.replyTime,
                item.msg,
                pojoAttribute.imageUrl,
                pojoAttribute.url,
                pojoAttribute.imageUrl2,
                pojoAttribute.url2,
                item.blastId
        )
    }

    private fun convertToQuotation(message: Reply): Visitable<*> {
        val quotationAttributes = GsonBuilder()
                .create()
                .fromJson<QuotationAttributes>(
                        message.attachment?.attributes,
                        QuotationAttributes::class.java
                )
        return QuotationUiModel(
                quotationPojo = quotationAttributes.quotation,
                messageId = message.msgId.toString(),
                fromUid = message.senderId.toString(),
                from = message.senderName,
                fromRole = message.role,
                attachmentId = message.attachment?.id ?: "",
                attachmentType = message.attachment?.type.toString(),
                replyTime = message.replyTime,
                isSender = !message.isOpposite,
                message = message.msg,
                isRead = message.isRead
        )
    }
}
