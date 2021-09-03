package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.common.fromJson
import com.tokopedia.topchat.common.getNextWeekTimestamp
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ChatAttachmentUseCaseStub @Inject constructor(
    private val gqlUseCase: GraphqlUseCaseStub<ChatAttachmentResponse>,
    mapper: ChatAttachmentMapper,
    dispatchers: CoroutineDispatchers
) : ChatAttachmentUseCase(gqlUseCase, mapper, dispatchers) {

    var response = ChatAttachmentResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    private val broadcastCampaignLabelPath = "chat_attachment_banner_label.json"

    fun broadcastCampaignStarted(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(start_date, getNextWeekTimestamp())
                    attr.addProperty(start_date, getNextWeekTimestamp())
                    attr.addProperty(campaign_label, "Broadcast dimulai")
                }
            )
        }
    }

    private fun alterAttachmentAttributesAt(
        position: Int,
        responseObj: JsonObject,
        attachmentAltercation: (JsonObject) -> Unit,
        attributesAltercation: (JsonObject) -> Unit
    ) {
        val attachment = responseObj.getAsJsonObject(chatAttachments)
            .getAsJsonArray(list).get(position).asJsonObject
        attachmentAltercation(attachment)
        val attr = attachment.get(attributes).asString
        val attrObj = CommonUtil.fromJson<JsonObject>(attr, JsonObject::class.java)
        attributesAltercation(attrObj)
        attachment.addProperty(attributes, attrObj.toString())
    }

    fun getCampaignLabel(response: ChatAttachmentResponse): String {
        val attr = response.chatAttachments.list[0].attributes
        return fromJson<ImageAnnouncementPojo>(attr).campaignLabel!!
    }

    private val chatAttachments = "chatAttachments"
    private val list = "list"
    private val attributes = "attributes"
    private val start_date = "start_date"
    private val end_date = "end_date"
    private val status_campaign = "status_campaign"
    private val id = "id"
    private val campaign_label = "campaign_label"

}