package com.tokopedia.play.broadcaster.analytic.interactive

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by mzennis on 22/07/21.
 */
class PlayBroadcastInteractiveAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroadcastInteractiveAnalytic {

    override fun onImpressInteractiveTool(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "impression on engagement button",
                KEY_EVENT_LABEL to "$channelId - live",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickInteractiveTool(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click engagement button",
                KEY_EVENT_LABEL to "$channelId - live",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onStartInteractive(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String,
        durationInMs: Long
    ) {
        val durationInSecond = TimeUnit.MILLISECONDS.toSeconds(durationInMs)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click terapkan & mulai button",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle - $durationInSecond",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onImpressWinnerIcon(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "impression on winner list",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickWinnerIcon(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click winner list",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickChatWinnerIcon(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - chat winners",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickGameIconButton(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - gamification button",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickGameOption(channelId: String, channelTitle: String, gameType: String) {
        sendClickEvent(
            "click - engagement widget",
            "${userSession.shopId} - $channelId - $channelTitle - $gameType",
        )
    }

    override fun onClickContinueGiveaway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - lanjut giveaway",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onImpressGameIconButton(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - gamification button",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickBackGiveaway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back gamification selection",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickGameResult(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - hasil game",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickCloseGameResultBottomsheet(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - x hasil game bottomsheet",
            "${userSession.shopId} - $channelId - $channelTitle"
        )
    }

    override fun onClickCloseGameResultReport(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - x report page gamification",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onImpressFailedLeaderboard(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - failed to load",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onClickRefreshGameResult(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - coba lagi hasil game",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onImpressSelectGame(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - engagement widget",
            "${userSession.shopId} - $channelId - $channelTitle",
            )
    }

    override fun onClickBackQuiz(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back quiz",
            "${userSession.shopId} - $channelId - $channelTitle",
        )
    }

    override fun onclickBackSetTimerGiveAway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back duration giveaway",
            "${userSession.shopId} - $channelId - $channelTitle",
            )
    }

    override fun onClickContinueQuiz(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - lanjut quiz",
            "${userSession.shopId} - $channelId - $channelTitle",
            )
    }

    override fun onClickQuizGift(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - giveaway optional",
            "${userSession.shopId} - $channelId - $channelTitle",
            )
    }

    private fun sendClickEvent(
        eventAction: String,
        eventLabel: String,
    ) {
        sendEvent(eventAction, eventLabel, KEY_TRACK_CLICK_EVENT)
    }

    private fun sendImpressionEvent(
        eventAction: String,
        eventLabel: String,
    ) {
        sendEvent(eventAction, eventLabel, KEY_TRACK_VIEW_EVENT)
    }

    private fun sendEvent(
        eventAction: String,
        eventLabel: String,
        eventKey: String,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to eventKey,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_LABEL to eventLabel,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId
            )
        )
    }
}