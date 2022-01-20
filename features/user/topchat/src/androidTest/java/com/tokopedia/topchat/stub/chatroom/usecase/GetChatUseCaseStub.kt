package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.BaseChatUiModel.Companion.STATUS_DELETED
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetChatUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatchers: CoroutineDispatchers
) : GetChatUseCase(repository, dispatchers) {

    private val changeAddressResponsePath =
        "success_get_chat_replies_with_srw_change_address.json"
    private val replyBubbleResponsePath =
        "success_get_chat_replies_with_reply_bubble.json"
    private val broadcastCampaignLabelPath =
        "success_get_chat_with_broadcast_campaign.json"
    private val chatWithSellerPath =
        "success_get_chat_first_page_as_seller.json"
    private val chatWithBuyerPath =
        "success_get_chat_first_page_as_buyer.json"
    private val bannedProductChatWithBuyerPath =
        "success_get_chat_first_page_with_banned_products.json"
    private val sellerSrwPromptPath =
        "seller/success_get_chat_replies_with_srw_reply_prompt.json"
    private val shippingLocationPath =
        "seller/chat_replies_shipping_location.json"
    private val upcomingCampaignPath =
        "buyer/chat_replies_upcoming_campaign.json"
    private val deleteImageResponsePath =
        "buyer/chat_replies_delete_image.json"
    private val blockedChatAsBuyer =
        "success_get_blocked_chat_replies.json"

    var response: GetExistingChatPojo = GetExistingChatPojo()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(response::class.java, "Oops!")
            }
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

    val bannedProductChatWithBuyerResponse: GetExistingChatPojo
        get() = alterResponseOf(bannedProductChatWithBuyerPath) { response ->
            alterDateToToday(response)
        }

    val blockedChatResponse: GetExistingChatPojo
        get() = alterResponseOf(blockedChatAsBuyer) { response ->
            alterDateToToday(response)
        }

    /**
     * <!--- Start OOS label --->
     */

    val upComingCampaignProduct: GetExistingChatPojo
        get() = alterResponseOf(upcomingCampaignPath) { response -> }

    /**
     * <!--- End OOS label --->
     */

    /**
     * <!--- Start Delete Image --->
     */

    val deleteImageResponse: GetExistingChatPojo
        get() = alterResponseOf(deleteImageResponsePath) { }

    val deleteImageResponseWithStatus5: GetExistingChatPojo
        get() = alterResponseOf(deleteImageResponsePath) { response ->
            alterRepliesAttribute(
                listPosition = 0,
                chatsPosition = 0,
                responseObj = response,
                altercation = { replies ->
                    replies[1].asJsonObject.addProperty(status, STATUS_DELETED)
                }
            )
        }

    /**
     * <!--- End Delete Image --->
     */

    /**
     * <!--- Start Shipping Location Seller --->
     */

    val withShippingInfo: GetExistingChatPojo
        get() = alterResponseOf(shippingLocationPath) { response -> }

    val withShippingInfoBuyer: GetExistingChatPojo
        get() = alterResponseOf(shippingLocationPath) { response ->
            swapInterlocutor(response)
            alterRepliesAttribute(
                listPosition = 0,
                chatsPosition = 0,
                responseObj = response,
                altercation = { replies ->
                    replies.forEach {
                        it.asJsonObject.addProperty(isOpposite, false)
                    }
                }
            )
        }

    /**
     * <!--- End Shipping Location Seller --->
     */

    /**
     * <!--- Start SRW Prompt --->
     */

    val defaultSrwPrompt: GetExistingChatPojo get() {
        return alterResponseOf(sellerSrwPromptPath) { response -> }
    }

    val noTriggerTextSrwPrompt: GetExistingChatPojo
        get() = alterResponseOf(sellerSrwPromptPath) { response ->
            val chatReplies = response.getAsJsonObject(chatReplies)
            chatReplies.addProperty("hasNext", true)
            alterRepliesAttribute(0, 0, response) {
                for (i in 0 until 2) {
                    it.addAll(it)
                }
            }
        }

    fun getInTheMiddleOfThePageSrwPrompt(triggerText: String): GetExistingChatPojo {
        return alterResponseOf(sellerSrwPromptPath) { response ->
            alterChatAttribute(0, 0, 1, response) { chat ->
                chat.addProperty(msg, triggerText)
            }
            val chatReplies = response.getAsJsonObject(chatReplies)
            chatReplies.addProperty("hasNext", true)
            chatReplies.addProperty("hasNextAfter", true)
        }
    }

    fun getSrwPromptWithTriggerText(triggerText: String): GetExistingChatPojo {
        return alterResponseOf(sellerSrwPromptPath) { response ->
            alterChatAttribute(0, 0, 1, response) { chat ->
                chat.addProperty(msg, triggerText)
            }
        }
    }

    fun carouselSrwPrompt(triggerText: String): GetExistingChatPojo {
        return alterResponseOf(sellerSrwPromptPath) { response ->
            alterRepliesAttribute(0, 0, response) {
                val product = it.get(0)
                val chats = it.deepCopy()
                it.removeAll { true }
                it.apply {
                    add(product)
                    addAll(chats)
                }
            }
            alterChatAttribute(0, 0, 2, response) { chat ->
                chat.addProperty(msg, triggerText)
            }
        }
    }

    fun multipleSrwPrompt(triggerText: String): GetExistingChatPojo {
        return alterResponseOf(sellerSrwPromptPath) { response ->
            alterRepliesAttribute(0, 0, response) {
                val product = it.get(0)
                val msg = it.get(1)
                val chats = it.deepCopy()
                it.removeAll { true }
                it.apply {
                    add(product)
                    add(msg)
                    addAll(chats)
                }
            }
            alterChatAttribute(0, 0, 1, response) { chat ->
                chat.addProperty(msg, triggerText)
            }
            alterChatAttribute(0, 0, 3, response) { chat ->
                chat.addProperty(msg, triggerText)
            }
        }
    }

    /**
     * <!--- End SRW Prompt --->
     */

    /**
     * <!--- Start Reply bubble --->
     */
    val defaultReplyBubbleResponse: GetExistingChatPojo
        get() = alterResponseOf(replyBubbleResponsePath) { }

    val longReplyBubbleResponse: GetExistingChatPojo
        get() = alterResponseOf(replyBubbleResponsePath) { response ->
            alterDateToToday(response)
            val replies = response.getAsJsonObject(chatReplies)
                .getAsJsonArray(list).get(0).asJsonObject
                .getAsJsonArray(chats).get(0).asJsonObject
                .getAsJsonArray(replies)
            for (i in 0..10) {
                val newReply = replies.first().deepCopy().asJsonObject
                val newMsg = newReply.get(msg).asString + " $i"
                newReply.addProperty(msg, newMsg)
                replies.add(newReply)
            }
        }

    val inTheMiddleReplyBubbleResponse: GetExistingChatPojo
        get() = alterResponseOf(replyBubbleResponsePath) { response ->
            alterDateToToday(response)
            val chatReplies = response.getAsJsonObject(chatReplies)
            chatReplies.addProperty("hasNext", true)
            chatReplies.addProperty("hasNextAfter", true)
        }

    val expiredReplyBubbleResponse: GetExistingChatPojo
        get() = alterResponseOf(replyBubbleResponsePath) { response ->
            alterDateToToday(response)
            val chatReplies = response.getAsJsonObject(chatReplies)
                .getAsJsonArray(list).get(0).asJsonObject
                .getAsJsonArray(chats).get(0).asJsonObject
                .getAsJsonArray(replies)
            val lastReplyParentReply = chatReplies.last().asJsonObject
                .getAsJsonObject(parentReply)
            lastReplyParentReply.addProperty(isExpired, true)
        }

    /**
     * <!--- End Reply bubble --->
     */

    /**
     * <!--- Start Broadcast responses --->
     */
    val defaultBroadcastCampaignLabel: GetExistingChatPojo
        get() = AndroidFileUtil.parse(
            broadcastCampaignLabelPath,
            GetExistingChatPojo::class.java
        )

    /**
     * <!--- End Broadcast responses --->
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
     * <!--- End SRW responses --->
     */

    fun getLastIndexOf(response: GetExistingChatPojo): Int {
        return response.chatReplies.list[0].chats[0].replies.lastIndex
    }

    private val chatReplies = "chatReplies"
    private val list = "list"
    private val chats = "chats"
    private val replies = "replies"
    private val msg = "msg"
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
    private val isExpired = "isExpired"
    private val parentReply = "parentReply"

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
        alterChatAttribute(listPosition, chatsPosition, repliesPosition, responseObj) {
            val attachment = it.getAsJsonObject(attachment)
            val attr = attachment.getAsJsonPrimitive(attributes).asString
            val attrObj = CommonUtil.fromJson<JsonObject>(attr, JsonObject::class.java)
            altercation(attrObj)
            attachment.addProperty(attributes, attrObj.toString())
        }
    }

    private fun alterChatAttribute(
        listPosition: Int,
        chatsPosition: Int,
        repliesPosition: Int,
        responseObj: JsonObject,
        altercation: (JsonObject) -> Unit
    ) {
        alterRepliesAttribute(listPosition, chatsPosition, responseObj) { replies ->
            val chat = replies.get(repliesPosition).asJsonObject
            altercation(chat)
        }
    }

    private fun alterRepliesAttribute(
        listPosition: Int,
        chatsPosition: Int,
        responseObj: JsonObject,
        altercation: (JsonArray) -> Unit
    ) {
        val replies = responseObj.getAsJsonObject(chatReplies)
            .getAsJsonArray(list).get(listPosition).asJsonObject
            .getAsJsonArray(chats).get(chatsPosition).asJsonObject
            .getAsJsonArray(replies)
        altercation(replies)
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

    private fun swapInterlocutor(response: JsonObject) {
        val contacts = response.getAsJsonObject(chatReplies)
            .getAsJsonArray(contacts)
        val interLoc = contacts.find { contact ->
            contact.asJsonObject.get(interlocutor).asBoolean
        }
        val notInterLoc = contacts.find { contact ->
            !contact.asJsonObject.get(interlocutor).asBoolean
        }
        interLoc?.asJsonObject?.addProperty(interlocutor, false)
        notInterLoc?.asJsonObject?.addProperty(interlocutor, true)
    }
}