package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel.CampaignStatus
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.common.*
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

    private val sellerSrwPromptPath =
        "seller/success_get_chat_attachment_srw_reply_prompt.json"
    private val defaultChatAttachmentResponsePath =
        "success_get_chat_attachments.json"
    private val shippingLocationPath =
        "seller/chat_attachment_shipping_location_reply.json"
    private val upcomingCampaignPath =
        "buyer/chat_attachment_upcoming_campaign.json"

    val chatAttachmentNoVariant: ChatAttachmentResponse
        get() = alterResponseOf(defaultChatAttachmentResponsePath) {
            alterAttachmentAttributesAt(
                position = 2,
                responseObj = it,
                attachmentAltercation = { },
                attributesAltercation = { attr ->
                    attr.getAsJsonObject(product_profile)
                        .addProperty(is_variant, false)
                }
            )
        }

    /**
     * <!--- Start Start OOS label --->
     */

    val upComingCampaignProduct: ChatAttachmentResponse
        get() = alterResponseOf(upcomingCampaignPath) { response -> }

    val notUpComingCampaignProductAndInactive: ChatAttachmentResponse
        get() = alterResponseOf(upcomingCampaignPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { },
                attributesAltercation = { attr ->
                    val productProfile = attr.getAsJsonObject(product_profile)
                    productProfile.let {
                        it.addProperty(is_upcoming_campaign_product, false)
                        it.addProperty(status, ProductAttachmentUiModel.statusDeleted)
                    }
                }
            )
        }

    val notUpComingCampaignProductAndActive: ChatAttachmentResponse
        get() = alterResponseOf(upcomingCampaignPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { },
                attributesAltercation = { attr ->
                    val productProfile = attr.getAsJsonObject(product_profile)
                    productProfile.let {
                        it.addProperty(is_upcoming_campaign_product, false)
                        it.addProperty(status, ProductAttachmentUiModel.statusActive)
                    }
                }
            )
        }

    /**
     * <!--- End Start OOS label --->
     */

    /**
     * <!--- Start Shipping Location Seller --->
     */

    val withShippingInfo: ChatAttachmentResponse
        get() = alterResponseOf(shippingLocationPath) { response -> }

    val withoutShippingInfo: ChatAttachmentResponse
        get() = alterResponseOf(shippingLocationPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { },
                attributesAltercation = { attr ->
                    attr.getAsJsonObject(product_profile)
                        .getAsJsonObject(location_stock)
                        .addProperty(district_name_full_text, "")
                }
            )
        }

    val withShippingInfoAndTokocabang: ChatAttachmentResponse
        get() = alterResponseOf(shippingLocationPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { },
                attributesAltercation = { attr ->
                    attr.getAsJsonObject(product_profile)
                        .addProperty(is_fulfillment, true)
                }
            )
        }

    fun getShippingText(position: Int, response: ChatAttachmentResponse): String {
        val attr = response.chatAttachments.list[position].attributes
        val obj = CommonUtil.fromJson<JsonObject>(attr, JsonObject::class.java)
        return obj.getAsJsonObject(product_profile)
            .getAsJsonObject(location_stock)
            .get(district_name_full_text)
            .asString
    }

    /**
     * <!--- End Shipping Location Seller --->
     */

    /**
     * <!--- Start SRW Prompt --->
     */

    val defaultSrwPrompt: ChatAttachmentResponse
        get() = alterResponseOf(sellerSrwPromptPath) { }

    /**
     * <!--- End SRW Prompt --->
     */

    fun createBroadcastCampaignStarted(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(start_date, getNextWeekTimestamp())
                    attr.addProperty(campaign_label, "Broadcast dimulai")
                    attr.addProperty(status_campaign, CampaignStatus.STARTED)
                }
            )
        }
    }

    fun createBroadcastCampaignOnGoing(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(end_date, getNext6Hours())
                    attr.addProperty(campaign_label, "Broadcast berlangsung")
                    attr.addProperty(status_campaign, CampaignStatus.ON_GOING)
                }
            )
        }
    }

    fun createBroadcastCampaignAboutToEnd(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(end_date, getNext3Seconds())
                    attr.addProperty(campaign_label, "Broadcast berlangsung")
                    attr.addProperty(wording_end_state, "Broadcast berakhir")
                    attr.addProperty(status_campaign, CampaignStatus.ON_GOING)
                }
            )
        }
    }

    fun createBroadcastCampaignEnded(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(wording_end_state, "Broadcast berakhir")
                    attr.addProperty(status_campaign, CampaignStatus.ENDED)
                }
            )
        }
    }

    fun createBroadcastNoCampaign(bannerAttachmentId: String): ChatAttachmentResponse {
        return alterResponseOf(broadcastCampaignLabelPath) { response ->
            alterAttachmentAttributesAt(
                position = 0,
                responseObj = response,
                attachmentAltercation = { attachment ->
                    attachment.addProperty(id, bannerAttachmentId)
                },
                attributesAltercation = { attr ->
                    attr.addProperty(is_campaign, false)
                }
            )
        }
    }

    fun getCampaignLabel(response: ChatAttachmentResponse): String {
        val attr = response.chatAttachments.list[0].attributes
        return fromJson<ImageAnnouncementPojo>(attr).campaignLabel!!
    }

    fun getEndWordingBannerFrom(response: ChatAttachmentResponse): String {
        val attr = response.chatAttachments.list[0].attributes
        return fromJson<ImageAnnouncementPojo>(attr).endStateWording!!
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

    private val chatAttachments = "chatAttachments"
    private val list = "list"
    private val attributes = "attributes"
    private val id = "id"
    private val product_profile = "product_profile"
    private val is_variant = "is_variant"
    private val location_stock = "location_stock"
    private val district_name_full_text = "district_name_full_text"
    private val is_upcoming_campaign_product = "is_upcoming_campaign_product"
    private val status = "status"
    private val is_fulfillment = "is_fulfillment"

    // broadcast attributes
    private val start_date = "start_date"
    private val end_date = "end_date"
    private val status_campaign = "status_campaign"
    private val campaign_label = "campaign_label"
    private val wording_end_state = "wording_end_state"
    private val is_campaign = "is_campaign"

}