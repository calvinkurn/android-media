package com.tokopedia.groupchat.room.domain.mapper

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.domain.mapper.StickyComponentMapper
import com.tokopedia.groupchat.chatroom.domain.pojo.*
import com.tokopedia.groupchat.chatroom.domain.pojo.EventHandlerPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.Channel
import com.tokopedia.groupchat.chatroom.domain.pojo.imageannouncement.AdminImagePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.ActivePollPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.sprintsale.FlashSalePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.sprintsale.Product
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel
import com.tokopedia.groupchat.vote.view.model.VoteViewModel
import com.tokopedia.websocket.WebSocketResponse
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 15/02/19
 */
class PlayWebSocketMessageMapper @Inject constructor() {

    lateinit var gson: Gson

    companion object {
        const val DEFAULT_NO_POLL = 0
        const val FORMAT_DISCOUNT_LABEL = "%d%% OFF"
    }

    fun mapHideMessage(response: WebSocketResponse): Boolean {
        return when (response.getType()?.toLowerCase()) {
            VoteAnnouncementViewModel.POLLING_CANCEL, VoteAnnouncementViewModel.POLLING_UPDATE,
            VibrateViewModel.TYPE, SprintSaleAnnouncementViewModel.SPRINT_SALE_UPCOMING,
            PinnedMessageViewModel.TYPE, AdsViewModel.TYPE, GroupChatQuickReplyViewModel.TYPE,
            EventHandlerPojo.BANNED, EventHandlerPojo.FREEZE, ParticipantViewModel.TYPE,
            OverlayViewModel.TYPE, OverlayCloseViewModel.TYPE, VideoViewModel.TYPE,
            DynamicButtonsViewModel.TYPE, StickyComponentViewModel.TYPE -> true
            else -> false
        }
    }

    fun map(response: WebSocketResponse): Visitable<*>? {
        val data = response.getData()
        if (response.getType() == null) {
            return null
        }

        gson = Gson()
        return when (response.getType()?.toLowerCase()) {
            VoteAnnouncementViewModel.POLLING_START,
            VoteAnnouncementViewModel.POLLING_FINISHED,
            VoteAnnouncementViewModel.POLLING_END,
            VoteAnnouncementViewModel.POLLING_UPDATE,
            VoteAnnouncementViewModel.POLLING_CANCEL ->
                mapToPollingViewModel(response, data)

            ChatViewModel.ADMM -> mapToAdminChat(data)

            ImageAnnouncementViewModel.ADMIN_ANNOUNCEMENT -> mapToAdminImageChat(data)
            SprintSaleAnnouncementViewModel.SPRINT_SALE_UPCOMING,
            SprintSaleAnnouncementViewModel.SPRINT_SALE_START,
            SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH -> mapToSprintSale(data, response)
            VibrateViewModel.TYPE -> VibrateViewModel()
            GeneratedMessageViewModel.TYPE -> mapToGeneratedMessage(data)
            PinnedMessageViewModel.TYPE -> mapToPinnedMessage(data)
            AdsViewModel.TYPE -> mapToAds(data)
            GroupChatQuickReplyViewModel.TYPE -> mapToQuickReply(data)
            VideoViewModel.TYPE -> mapToVideo(data)
            ChatViewModel.USER_MESSAGE -> mapToUserChat(data)
            EventHandlerPojo.BANNED, EventHandlerPojo.FREEZE -> mapToEventHandler(data)
            ParticipantViewModel.TYPE -> mapToParticipant(data)
            OverlayViewModel.TYPE -> mapToOverlay(data)
            OverlayCloseViewModel.TYPE -> mapToOverlayClose(data)
            DynamicButtonsViewModel.TYPE -> mapToDynamicButton(data)
            BackgroundViewModel.TYPE -> mapToBackground(data)
            StickyComponentViewModel.TYPE -> mapToStickyComponent(data)
            StickyComponentViewModel.TYPE_CLOSE -> StickyComponentViewModel()
            else -> null
        }
    }

    private fun mapToStickyComponent(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, StickyComponentData::class.java)
        return StickyComponentMapper().mapToViewModel(pojo)
    }

    private fun mapToBackground(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, BackgroundViewModel::class.java)
        return pojo
    }

    private fun mapToDynamicButton(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, ButtonsPojo::class.java)
        return convertDynamicButtons(pojo)
    }

    private fun convertDynamicButtons(button: ButtonsPojo): DynamicButtonsViewModel {
        val dynamicButtonsViewModel = DynamicButtonsViewModel()
        button.floatingButton?.let {
            dynamicButtonsViewModel.floatingButton = DynamicButton(
                    it.buttonId,
                    it.imageUrl,
                    it.linkUrl,
                    it.contentType,
                    it.contentText,
                    it.contentButtonText,
                    it.contentLinkUrl,
                    it.contentImageUrl,
                    it.redDot,
                    it.tooltip
            )
        }

        button.listDynamicButton?.let{
            for (buttonItem in it) {
                dynamicButtonsViewModel.listDynamicButton.add(
                        DynamicButton(
                                buttonItem.buttonId,
                                buttonItem.imageUrl,
                                buttonItem.linkUrl,
                                buttonItem.contentType,
                                buttonItem.contentText,
                                buttonItem.contentButtonText,
                                buttonItem.contentLinkUrl,
                                buttonItem.contentImageUrl,
                                buttonItem.redDot,
                                buttonItem.tooltip
                        )
                )
            }
        }

        button.interactiveButton?.let {
            dynamicButtonsViewModel.interactiveButton = InteractiveButton(
                    it.isEnabled,
                    it.listBalloon
            )
        }

        return dynamicButtonsViewModel
    }


    private fun mapToUserChat(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, UserMsg::class.java)
        pojo.user?.let {
            return ChatViewModel(
                    pojo.message,
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    pojo.messageId,
                    it.id,
                    it.name,
                    it.image,
                    false,
                    false
            )
        }
        return null
    }

    private fun mapToPinnedMessage(json: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(json.toString())) {
            return PinnedMessageViewModel("", "", "", "")
        }
        val pinnedMessage = gson.fromJson(json, PinnedMessagePojo::class.java)
        return PinnedMessageViewModel(pinnedMessage.message, pinnedMessage.title, pinnedMessage.redirectUrl, pinnedMessage.imageUrl)
    }

    private fun mapToAds(json: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(json.toString())) {
            return AdsViewModel("", "", "")
        }
        return gson.fromJson(json, AdsViewModel::class.java)
    }

    private fun mapToQuickReply(json: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(json.toString())) {
            return GroupChatQuickReplyViewModel()
        }
        val gson = Gson()
        val channel = gson.fromJson(json, Channel::class.java)
        val model = GroupChatQuickReplyViewModel()

        val list = ArrayList<GroupChatQuickReplyItemViewModel>()
        if (channel?.listQuickReply != null) {
            var id = 1
            for (quickReply in channel.listQuickReply) {
                val item = GroupChatQuickReplyItemViewModel(id.toString(), quickReply)
                list.add(item)
                id++
            }
        }
        model.list = list

        return model
    }

    private fun mapToVideo(data: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(data.toString())) {
            return VideoViewModel("")
        }
        val gson = Gson()
        return gson.fromJson(data, VideoViewModel::class.java)
    }

    private fun mapToOverlay(data: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(data.toString())) {
            return OverlayViewModel()
        }
        val gson = Gson()
        return gson.fromJson(data, OverlayViewModel::class.java)
    }

    private fun mapToOverlayClose(data: JsonObject?): Visitable<*> {
        if (TextUtils.isEmpty(data.toString())) {
            return OverlayCloseViewModel()
        }
        val gson = Gson()
        return gson.fromJson(data, OverlayCloseViewModel::class.java)
    }


    private fun mapToSprintSale(data: JsonObject?, response: WebSocketResponse): Visitable<*>? {
        val pojo = gson.fromJson(data, FlashSalePojo::class.java)

        pojo.user?.let {
            return SprintSaleAnnouncementViewModel(
                    pojo.campaignId,
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    pojo.messageId.toString(),
                    it.id,
                    it.name,
                    it.image,
                    false,
                    true,
                    if (pojo.appLink != null) pojo.appLink else "",
                    mapToListSprintSaleProducts(pojo.campaignId, pojo.products),
                    if (pojo.campaignName != null) pojo.campaignName else "",
                    pojo.startDate * 1000L,
                    pojo.endDate * 1000L,
                    response.getType()!!
            )
        }

        return null
    }

    private fun mapToListSprintSaleProducts(campaignId: String, pojo: List<Product>): ArrayList<SprintSaleProductViewModel> {
        val list = ArrayList<SprintSaleProductViewModel>()
        for (product in pojo) {
            list.add(mapToSprintSaleProduct(campaignId, product))
        }
        return list
    }

    private fun mapToSprintSaleProduct(campaignId: String?, product: Product): SprintSaleProductViewModel {
        return SprintSaleProductViewModel(
                campaignId ?: "",
                if (product.productId != null) product.productId else "",
                if (product.name != null) product.name else "",
                if (product.imageUrl != null) product.imageUrl else "",
                String.format(Locale.getDefault(), FORMAT_DISCOUNT_LABEL, product.discountPercentage),
                if (product.discountedPrice != null) product.discountedPrice else "",
                if (product.originalPrice != null) product.originalPrice else "",
                product.remainingStockPercentage,
                if (product.stockText != null) product.stockText else "",
                if (product.urlMobile != null) product.urlMobile else "")
    }

    private fun mapToParticipant(data: JsonObject?): Visitable<*> {
        val pojo = gson.fromJson(data, ParticipantPojo::class.java)
        return ParticipantViewModel(pojo.channelId, pojo.totalView)
    }

    private fun mapToEventHandler(data: JsonObject?): Visitable<*> {
        val pojo = gson.fromJson(data, com.tokopedia.groupchat.chatroom.domain.pojo.EventHandlerPojo::class.java)

        return EventGroupChatViewModel(pojo.isFreeze, pojo.isBanned, pojo.channelId, pojo.userId)
    }

    private fun mapToGeneratedMessage(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, GeneratedMessagePojo::class.java)

        pojo.user?.let {
            return GeneratedMessageViewModel(
                    pojo.message,
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    pojo.messageId.toString(),
                    it.id,
                    it.name,
                    it.image,
                    false,
                    true
            )
        }

        return null
    }

    private fun mapToAdminImageChat(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson(data, AdminImagePojo::class.java)

        pojo.user?.let {
            return ImageAnnouncementViewModel(
                    pojo.imageId,
                    pojo.imageUrl.trim { it <= ' ' },
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    pojo.messageId,
                    it.id,
                    it.name,
                    it.image,
                    false,
                    true,
                    pojo.redirectUrl
            )
        }
        return null
    }

    private fun mapToAdminChat(data: JsonObject?): Visitable<*>? {
        val pojo = gson.fromJson<AdminMsg>(data, AdminMsg::class.java)

        pojo.user?.let {
            return ChatViewModel(
                    pojo.message,
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    pojo.messageId.toString(),
                    it.id,
                    it.name,
                    it.image,
                    false,
                    true
            )
        }

        return null
    }

    private fun mapToPollingViewModel(response: WebSocketResponse, data: JsonObject?): VoteAnnouncementViewModel? {
        val pojo = gson.fromJson(data, ActivePollPojo::class.java)

        pojo.user?.let {
            return VoteAnnouncementViewModel(
                    pojo.description,
                    response.getType()!!,
                    pojo.timestamp!!,
                    pojo.timestamp!!,
                    "",
                    it.id,
                    it.name,
                    it.image,
                    false,
                    true,
                    mappingToVoteInfoViewModel(pojo)
            )
        }

        return null
    }

    private fun mappingToVoteInfoViewModel(activePollPojo: ActivePollPojo): VoteInfoViewModel? {
        return if (hasPoll(activePollPojo)) {
            VoteInfoViewModel(
                    activePollPojo.pollId.toString(),
                    activePollPojo.title,
                    activePollPojo.question,
                    null,
                    activePollPojo.statistic?.totalVoter.toString(),
                    activePollPojo.pollType,
                    VoteViewModel.BAR_TYPE,
                    activePollPojo.status,
                    activePollPojo.statusId,
                    activePollPojo.isIsAnswered,
                    VoteInfoViewModel.getStringVoteInfo(activePollPojo.pollTypeId),
                    activePollPojo.winnerUrl.trim { it <= ' ' },
                    activePollPojo.startTime,
                    activePollPojo.endTime,
                    activePollPojo.voteUrl
            )
        } else {
            null
        }
    }

    private fun hasPoll(activePoll: ActivePollPojo?): Boolean {
        return (!(activePoll?.statistic == null || activePoll.pollId == DEFAULT_NO_POLL))
    }

}