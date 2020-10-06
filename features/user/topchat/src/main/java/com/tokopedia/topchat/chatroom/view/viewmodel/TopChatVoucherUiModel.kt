package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

/**
 * Created by Steven on 18/03/19.
 */
class TopChatVoucherUiModel
/**
 * Constructor for WebSocketResponse / API Response
 *
 * @param messageId         messageId
 * @param fromUid           userId of sender
 * @param from              name of sender
 * @param fromRole          role of sender
 * @param attachmentId      attachment id
 * @param attachmentType    attachment type. Please refer to
 * @param replyTime         replytime in unixtime
 * @param imageUrlTop       image url Top image
 * @param redirectUrlTop    redirect url in http for Top image click
 * @param imageUrlBottom    image url Bottom image
 * @param redirectUrlBottom redirect url in http for Bottom image click
 * @param blastId           blast id for campaign.
 */
constructor(
        messageId: String, fromUid: String, from: String, fromRole: String,
        attachmentId: String, attachmentType: String, replyTime: String, message: String,
        isRead: Boolean, isDummy: Boolean, isSender: Boolean, var voucherModel: MerchantVoucherModel,
        replyId: String, var blastId: String, source: String, val isPublic: Int
) : SendableViewModel(
        messageId, fromUid, from, fromRole,
        attachmentId, attachmentType, replyTime, "",
        isRead, isDummy, isSender, message,
        source, replyId
), Visitable<TopChatTypeFactory> {

    val voucher: MerchantVoucherViewModel = MerchantVoucherViewModel(voucherModel)

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasCtaCopy(): Boolean {
        return isPublic == 0
    }
}
