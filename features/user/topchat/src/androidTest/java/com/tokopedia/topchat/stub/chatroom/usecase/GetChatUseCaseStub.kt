package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import java.text.SimpleDateFormat
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
    private val chatWithSellerPath =
        "success_get_chat_first_page_as_seller.json"
    private val chatWithBuyerPath =
        "success_get_chat_first_page_as_buyer.json"

    var response: GetExistingChatPojo = GetExistingChatPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    val defaultChatWithSellerResponse: GetExistingChatPojo
        get() = alterResponseOf(chatWithSellerPath) { response ->
            alterDateToToday(response)
        }

    val defaultChatWithBuyerResponse: GetExistingChatPojo
        get() = alterResponseOf(chatWithBuyerPath) { response ->
            alterDateToToday(response)
        }

    var isError = false
        set(value) {
            gqlUseCase.isError = value
            field = value
        }

    /**
     * <!--- Start Broadcast responses --->
     */
    val defaultBroadcastCampaignLabel: GetExistingChatPojo
        get() = AndroidFileUtil.parse(
            broadcastCampaignLabelPath,
            GetExistingChatPojo::class.java
        )
    /**
     * <!--- Stop Broadcast responses --->
     */

    fun getBannerAttachmentId(response: GetExistingChatPojo): String {
        return response.chatReplies.list[0].chats[0].replies[0].attachment.id
    }

    /**
     * <!--- Start SRW responses --->
     */
    val defaultChangeAddressResponse: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            alterDateToToday(response)
        }

    val srwChangeAddressCtaDisabled: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            alterHeaderCtaButtonAttachment(
                listPosition = 0,
                chatsPosition = 0,
                repliesPosition = 0,
                responseObj = response
            ) { attr ->
                attr.addProperty(status, HeaderCtaMessageAttachment.STATUS_DISABLED)
                attr.addProperty(text_url, "Disabled")
            }
        }

    val srwChangeAddressBodyMsg: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            alterHeaderCtaButtonAttachment(
                listPosition = 0,
                chatsPosition = 0,
                repliesPosition = 0,
                responseObj = response
            ) { attr ->
                attr.addProperty(body, "Attachment Body Msg")
            }
        }

    val srwChangeAddressNoCta: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            alterHeaderCtaButtonAttachment(
                listPosition = 0,
                chatsPosition = 0,
                repliesPosition = 0,
                responseObj = response
            ) { attr ->
                attr.addProperty(type, HeaderCtaMessageAttachment.TYPE_NO_BUTTON)
            }
        }

    val srwChangeAddressSeller: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            // swap interlocutor between seller and buyer
            val contacts = response.getAsJsonObject(chatReplies)
                .getAsJsonArray(contacts)
            val buyer = contacts.get(0)
            val seller = contacts.get(1)
            buyer.asJsonObject.addProperty(interlocutor, true)
            seller.asJsonObject.addProperty(interlocutor, false)
            // change isOpposite to false
            response.getAsJsonObject(chatReplies)
                .getAsJsonArray(list).get(0).asJsonObject
                .getAsJsonArray(chats).get(0).asJsonObject
                .getAsJsonArray(replies).get(0).asJsonObject
                .addProperty(isOpposite, false)
        }

    val nullAttachment: GetExistingChatPojo
        get() = alterResponseOf(changeAddressResponsePath) { response ->
            response.getAsJsonObject(chatReplies)
                .getAsJsonArray(list).get(0).asJsonObject
                .getAsJsonArray(chats).get(0).asJsonObject
                .getAsJsonArray(replies).get(0).asJsonObject
                .remove(attachment)
        }
    /**
     * <!--- Stop SRW responses --->
     */

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
    private val date = "date"

    private fun alterDateToToday(response: JsonObject) {
        val list = response.getAsJsonObject(chatReplies)
            .getAsJsonArray(list)
        list.forEach {
            val chat = it.asJsonObject
            chat.addProperty(date, getTodayDate())
        }
    }

    private fun getTodayDate(): String {
        val format = SimpleDateFormat(HeaderDateUiModel.DATE_FORMAT, Locale.ENGLISH)
        val currentTime = Calendar.getInstance().time
        return format.format(currentTime)
    }

    private fun alterHeaderCtaButtonAttachment(
        listPosition: Int,
        chatsPosition: Int,
        repliesPosition: Int,
        responseObj: JsonObject,
        altercation: (JsonObject) -> Unit
    ) {
        alterAttachmentAttributesAt(
            listPosition = listPosition,
            chatsPosition = chatsPosition,
            repliesPosition = repliesPosition,
            responseObj = responseObj
        ) {
            val ctaButton = it.getAsJsonObject(cta_button)
            altercation(ctaButton)
        }
    }

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