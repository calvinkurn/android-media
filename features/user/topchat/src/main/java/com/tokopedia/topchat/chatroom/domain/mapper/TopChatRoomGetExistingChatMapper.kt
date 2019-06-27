package com.tokopedia.topchat.chatroom.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_DUAL_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatlist.data.TopChatUrl
import com.tokopedia.topchat.chatroom.domain.pojo.ImageDualAnnouncementPojo
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel
import javax.inject.Inject

/**
 * @author : Steven 02/01/19
 */

open class TopChatRoomGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun map(pojo: GetExistingChatPojo): ChatroomViewModel {
        val chatroomViewModel = super.map(pojo)
        if (!pojo.chatReplies.hasNext) {
            chatroomViewModel.listChat.add(chatroomViewModel.listChat.size,
                    SecurityInfoViewModel(TopChatUrl.SECURITY_INFO_URL))
        }

        return chatroomViewModel
    }

    override fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_IMAGE_DUAL_ANNOUNCEMENT -> convertToDualAnnouncement(chatItemPojoByDateByTime)
            TYPE_VOUCHER -> convertToVoucher(chatItemPojoByDateByTime)
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

        return TopChatVoucherViewModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id.toString(),
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
        return ImageDualAnnouncementViewModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id.toString(),
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
}
