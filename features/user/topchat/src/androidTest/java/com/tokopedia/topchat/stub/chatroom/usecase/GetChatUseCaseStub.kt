package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel.CampaignStatus
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import java.util.*
import javax.inject.Inject

class GetChatUseCaseStub @Inject constructor(
    private val gqlUseCase: GraphqlUseCaseStub<GetExistingChatPojo>,
    mapper: TopChatRoomGetExistingChatMapper,
    dispatchers: CoroutineDispatchers
) : GetChatUseCase(gqlUseCase, mapper, dispatchers) {

    private val changeAddressResponsePath =
        "success_get_chat_replies_with_srw_change_address.json"
    private val broadcastCampaignLabelPath =
        "success_get_chat_with_broadcast_campaign.json"

    var response: GetExistingChatPojo = GetExistingChatPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    val defaultBroadcastCampaignLabel: GetExistingChatPojo
        get() = AndroidFileUtil.parse(
            broadcastCampaignLabelPath,
            GetExistingChatPojo::class.java
        )

    val broadcastCampaignStarted: GetExistingChatPojo
        get() = alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                listPosition = 0,
                chatsPosition = 0,
                repliesPosition = 0,
                responseObj = response
            ) { attr ->
                attr.addProperty(startDate, getNextWeekTimestamp())
                attr.addProperty(status_campaign, CampaignStatus.STARTED)
            }
        }

    /**
     * return next week timestamp in seconds
     */
    private fun getNextWeekTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return calendar.timeInMillis / 1_000
    }

    private val chatReplies = "chatReplies"
    private val list = "list"
    private val chats = "chats"
    private val replies = "replies"
    private val attachment = "attachment"
    private val attributes = "attributes"
    private val status = "status"
    private val text_url = "text_url"
    private val body = "body"
    private val type = "type"
    private val contacts = "contacts"
    private val interlocutor = "interlocutor"
    private val name = "name"
    private val isOpposite = "isOpposite"
    private val cta_button = "cta_button"
    // broadcast campaign label
    private val startDate = "startDate"
    private val status_campaign = "status_campaign"

    private fun alterAttachmentAttributesAt(
        listPosition: Int,
        chatsPosition: Int,
        repliesPosition: Int,
        responseObj: JsonObject,
        altercation: (JsonObject) -> Unit
    ) {
        val attachment = responseObj.getAsJsonObject(chatReplies)
            .getAsJsonArray(list).get(listPosition).asJsonObject
            .getAsJsonArray(chats).get(chatsPosition).asJsonObject
            .getAsJsonArray(replies).get(repliesPosition).asJsonObject
            .getAsJsonObject(attachment)
        val attr = attachment.getAsJsonPrimitive(attributes).asString
        val attrObj = CommonUtil.fromJson<JsonObject>(attr, JsonObject::class.java)
        altercation(attrObj)
        attachment.addProperty(attributes, attrObj.toString())
    }

    private fun alterResponseOf(
        responsePath: String,
        altercation: (JsonObject) -> Unit
    ): GetExistingChatPojo {
        val responseObj: JsonObject = AndroidFileUtil.parse(
            responsePath, JsonObject::class.java
        )
        altercation(responseObj)
        return CommonUtil.fromJson(
            responseObj.toString(), GetExistingChatPojo::class.java
        )
    }
}