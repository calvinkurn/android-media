package com.tokopedia.play.analytic.interactive

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 17/06/22
 */
class PlayInteractiveAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayInteractiveAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun clickFollowShopInteractive(
        interactiveId: String,
        gameType: GameUiModel,
        shopId: String,
        channelId: String,
        channelType: PlayChannelType
    ) {
        val (eventAction, eventLabel) = when (gameType) {
            is GameUiModel.Quiz -> Pair("click - follow quiz popup", "$shopId - $channelId - $userId - $interactiveId")
            is GameUiModel.Giveaway -> Pair("click follow from engagement tools widget", "$channelId - $channelType - $interactiveId")
            else -> Pair("", "")
        }
        sendCompleteGeneralEvent(
            event = if (gameType is GameUiModel.Quiz) Event.clickContent else Event.clickGroupChat,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = eventAction,
            eventLabel = eventLabel
        )
    }

    override fun impressFollowShopInteractive(
        shopId: String,
        gameType: GameUiModel,
        channelId: String
    ) {
        if (gameType !is GameUiModel.Quiz) return
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - follow quiz popup",
            eventLabel = "$shopId - $channelId - $userId - ${gameType.id}"
        )
    }

    override fun clickWinnerBadge(shopId: String, gameType: GameUiModel, interactiveId: String, channelId: String, channelType: PlayChannelType) {
        val (eventAction, eventLabel) = when (gameType) {
            is GameUiModel.Giveaway -> Pair("click daftar pemenang on engagement tools widget", "$channelId - $channelType")
            else -> Pair("click - hasil game button", "$shopId - $channelId - $userId - $interactiveId")
        }

        sendCompleteGeneralEvent(
            event = if (gameType is GameUiModel.Giveaway) Event.clickGroupChat else Event.clickContent,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = eventAction,
            eventLabel = eventLabel
        )
    }

    override fun impressWinnerBadge(
        shopId: String,
        interactiveId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - hasil game button",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun clickTapTap(
        interactiveId: String,
        channelId: String,
        channelType: PlayChannelType
    ) {
        sendCompleteGeneralEvent(
            event = Event.clickGroupChat,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "click tap terus icon",
            eventLabel = "$channelId - $channelType - $interactiveId"
        )
    }

    override fun clickRefreshLeaderBoard(
        interactiveId: String,
        shopId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.clickContent,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "click - refresh button leaderboard bottomsheet",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun impressRefreshLeaderBoard(
        interactiveId: String,
        shopId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - refresh button leaderboard bottomsheet",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun clickQuizOption(
        choiceAlphabet: String,
        interactiveId: String,
        shopId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.clickContent,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "click - multiple choice quiz popup",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId - $choiceAlphabet"
        )
    }

    override fun impressQuizOptions(
        interactiveId: String,
        shopId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - multiple choice quiz popup",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun clickActiveInteractive(
        interactiveId: String,
        shopId: String,
        gameType: GameUiModel,
        channelId: String
    ) {
        if (gameType !is GameUiModel.Quiz) return
        sendCompleteGeneralEvent(
            event = Event.clickContent,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "click - quiz widget",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun impressActiveInteractive(
        interactiveId: String,
        shopId: String,
        channelId: String
    ) {
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - quiz widget",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    override fun impressLeaderBoard(interactiveId: String, shopId: String, channelId: String) {
        sendCompleteGeneralEvent(
            event = Event.viewContentIris,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - quiz leaderboard bottomsheet",
            eventLabel = "$shopId - $channelId - $userId - $interactiveId"
        )
    }

    private fun sendCompleteGeneralEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to event,
                Key.eventCategory to eventCategory,
                Key.eventAction to eventAction,
                Key.eventLabel to eventLabel,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }
}
