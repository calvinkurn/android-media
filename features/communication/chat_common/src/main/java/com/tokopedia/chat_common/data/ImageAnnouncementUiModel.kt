package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel.CampaignStatus.ENDED
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel.CampaignStatus.ON_GOING
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel.CampaignStatus.STARTED
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.utils.time.TimeHelper

/**
 * @author by nisie on 5/15/18.
 */
class ImageAnnouncementUiModel
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
 * @param broadcastBlastId        blast id for campaign
 * @see AttachmentType for attachment types.
 */
constructor(
    messageId: String, fromUid: String, from: String, fromRole: String,
    attachmentId: String, attachmentType: String, replyTime: String, val imageUrl: String,
    val redirectUrl: String, message: String, val broadcastBlastId: Long, source: String,
    val isHideBanner: Boolean
) : BaseChatUiModel(
        messageId, fromUid, from, fromRole, attachmentId,
        attachmentType, replyTime, message, source
), Visitable<BaseChatTypeFactory>, DeferredAttachment {

    var campaignLabel: String = ""
        private set
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

    val endDateMillis get() = endDate.toLongOrZero() * 1_000
    val startDateMillis get() = startDate.toLongOrZero() * 1_000

    val startDateFormatted: String
        get() {
            return TimeHelper.format(startDateMillis, startDateFormat, TimeHelper.localeID)
        }

    private var startDateFormat = "dd MMM yyyy"

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
        broadcastBlastId = item.blastId,
        source = item.source
    ) {
        assignCampaignData(attributes)
    }

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String get() = attachmentId

    override fun updateData(attribute: Any?) {
        if (attribute is ImageAnnouncementPojo) {
            assignCampaignData(attribute)
        }
    }

    private fun assignCampaignData(attributes: ImageAnnouncementPojo) {
        attributes.endStateWording?.let {
            this.finishedDescription = it
        }
        attributes.isCampaign?.let {
            this.isCampaign = it
        }
        attributes.statusCampaign?.let {
            this.statusCampaign = it
        }
        attributes.startDate?.let {
            this.startDate = it
        }
        attributes.endDate?.let {
            this.endDate = it
        }
        attributes.campaignLabel?.let {
            this.campaignLabel = it
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasStartedCampaign(): Boolean {
        return statusCampaign == STARTED
    }

    fun hasOngoingCampaign(): Boolean {
        return statusCampaign == ON_GOING
    }

    fun hasEndedCampaign(): Boolean {
        return statusCampaign == ENDED
    }

    fun endCampaign() {
        statusCampaign = ENDED
    }

    fun eligibleToRenderCampaignLabel(): Boolean {
        return isCampaign && hasSupportedCampaignStatus()
    }

    fun hasSupportedCampaignStatus(): Boolean {
        return when (statusCampaign) {
            STARTED, ON_GOING, ENDED -> true
            else -> false
        }
    }

    object CampaignStatus {
        const val STARTED = 2
        const val ON_GOING = 3
        const val ENDED = 4
    }
}
