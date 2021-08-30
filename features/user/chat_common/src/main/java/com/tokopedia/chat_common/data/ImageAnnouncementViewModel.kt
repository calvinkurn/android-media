package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.utils.time.TimeHelper

/**
 * @author by nisie on 5/15/18.
 */
class ImageAnnouncementViewModel
/**
 * Constructor for WebSocketResponse / API Response
 *
 * @param messageId      messageId
 * @param fromUid        userId of sender
 * @param from           name of sender
 * @param fromRole       role of sender
 * @param attachmentId   attachment id
 * @param attachmentType attachment type.
 * @param replyTime      replytime in unixtime
 * @param imageUrl       image url
 * @param redirectUrl    redirect url in http
 * @param blastId        blast id for campaign
 * @see AttachmentType for attachment types.
 */
constructor(
        messageId: String, fromUid: String, from: String, fromRole: String,
        attachmentId: String, attachmentType: String, replyTime: String, val imageUrl: String,
        val redirectUrl: String, message: String, val blastId: Long, source: String,
        val isHideBanner: Boolean
) : BaseChatViewModel(
        messageId, fromUid, from, fromRole, attachmentId,
        attachmentType, replyTime, message, source
), Visitable<BaseChatTypeFactory> {

    var finishedDescription: String = ""
        private set
    var isCampaign: Boolean = false
        private set
    var statusCampaign: Int = 1
        private set
    var startDate: String = ""
        private set
    var endDate: String = ""
        private set

    val endDataMillis get() = endDate.toLongOrZero() * 1_000
    val startDataMillis get() = startDate.toLongOrZero() * 1_000

    val startDateFormatted: String
        get() {
            return TimeHelper.format(startDataMillis, "dd MMM yyyy", TimeHelper.localeID)
        }

    constructor(
        item: Reply, attributes: ImageAnnouncementPojo
    ) : this(
        messageId = item.msgId.toString(),
        fromUid = item.senderId.toString(),
        from = item.senderName,
        fromRole = item.role,
        attachmentId = item.attachment.id,
        attachmentType = item.attachment.type.toString(),
        replyTime = item.replyTime,
        imageUrl = attributes.imageUrl,
        redirectUrl = attributes.url,
        isHideBanner = attributes.isHideBanner,
        message = item.msg,
        blastId = item.blastId,
        source = item.source
    ) {
        this.finishedDescription = attributes.endStateWording
        this.isCampaign = attributes.isCampaign
        this.statusCampaign = attributes.statusCampaign
        this.startDate = attributes.startDate
        this.endDate = attributes.endDate
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasStartedCampaign(): Boolean {
        return statusCampaign == CampaignStatus.STARTED
    }

    fun hasOngoingCampaign(): Boolean {
        return statusCampaign == CampaignStatus.ON_GOING
    }

    fun endCampaign() {
        statusCampaign = CampaignStatus.ENDED
    }

    object CampaignStatus {
        const val STARTED = 1
        const val ON_GOING = 2
        const val ENDED = 3
    }
}
