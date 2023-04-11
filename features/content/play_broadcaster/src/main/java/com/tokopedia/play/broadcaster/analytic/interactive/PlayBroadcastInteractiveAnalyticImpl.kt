package com.tokopedia.play.broadcaster.analytic.interactive

import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_VIEW_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_SESSION_IRIS
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_SHOP_ID
import com.tokopedia.play.broadcaster.analytic.KEY_TRACKER_ID
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT
import com.tokopedia.play.broadcaster.analytic.sessionIris
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by mzennis on 22/07/21.
 */
class PlayBroadcastInteractiveAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val hydraConfig: HydraConfigStore,
) : PlayBroadcastInteractiveAnalytic {

    private val shopId = userSession.shopId
    private val authorId: String
        get() = hydraConfig.getAuthorId()
    private val authorTypeName: String
        get() = hydraConfig.getAuthorTypeName()


    override fun onImpressInteractiveTool(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "impression on engagement button",
                KEY_EVENT_LABEL to "$channelId - live",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to shopId,
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
                KEY_SHOP_ID to shopId,
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
                KEY_SHOP_ID to shopId,
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
                KEY_SHOP_ID to shopId,
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
                KEY_SHOP_ID to shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickChatWinnerIcon(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - chat winners",
            "shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickGameIconButton(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - gamification button",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickGameOption(channelId: String, channelTitle: String, gameType: String) {
        sendClickEvent(
            "click - engagement widget",
            "$shopId - $channelId - $channelTitle - $gameType",
        )
    }

    override fun onClickContinueGiveaway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - lanjut giveaway",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onImpressGameIconButton(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - gamification button",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickBackGiveaway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back gamification selection",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickGameResult(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - hasil game",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickCloseGameResultBottomsheet(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - x hasil game bottomsheet",
            "$shopId - $channelId - $channelTitle"
        )
    }

    override fun onClickCloseGameResultReport(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - x report page gamification",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onImpressFailedLeaderboard(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - failed to load",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickRefreshGameResult(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - coba lagi hasil game",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onImpressSelectGame(channelId: String, channelTitle: String) {
        sendImpressionEvent(
            "view - engagement widget",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickBackQuiz(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back quiz",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onclickBackSetTimerGiveAway(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back duration giveaway",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickContinueQuiz(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - lanjut quiz",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickStartQuiz(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - start quiz",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onClickBackQuizDuration(channelId: String, channelTitle: String) {
        sendClickEvent(
            "click - back duration quiz",
            "$shopId - $channelId - $channelTitle",
        )
    }

    override fun onImpressOngoingQuizWidget(
        channelId: String,
        channelTitle: String,
        quizId: String,
        quizTitle: String
    ) {
        sendImpressionEvent(
            "view - ongoing quiz widget",
            "$shopId - $channelId - $channelTitle - $quizId - $quizTitle",

            )
    }

    override fun onCLickQuizOptionLive(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendClickEvent(
            "click - quiz result option live room",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
        )
    }

    override fun onClickCloseOngoingQuizBottomSheet(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendClickEvent(
            "click - close quiz result",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
        )
    }

    override fun onClickBackQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendClickEvent(
            "click - close detail quiz answer",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",

            )
    }

    override fun onClickOngoingQuiz(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendClickEvent(
            "click - ongoing quiz widget",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
        )
    }

    override fun onImpressOngoingLeaderBoard(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String,
        reward: String
    ) {
        val prizes = if (reward.isNotBlank()) "prize" else "no prize"
        sendImpressionEvent(
            "view - quiz result live room",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle - $prizes",
        )
    }

    override fun onImpressQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendImpressionEvent(
            "view - quiz result option live room",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
        )
    }

    override fun onImpressReportLeaderboard(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String,
        engagementType: String
    ) {
        sendImpressionEvent(
            "view - quiz result report page",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle - $engagementType",
        )
    }

    override fun onImpressReportQuizChoiceDetail(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendImpressionEvent(
            "view - quiz result option report page",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
        )
    }

    override fun onCLickQuizOptionReport(
        channelId: String,
        channelTitle: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        sendClickEvent(
            "click - quiz result option report page",
            "$shopId - $channelId - $channelTitle - $interactiveId - $interactiveTitle",
            )
    }

    override fun onImpressedProductCarousel() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_EVENT)
            .setEventAction("impressed - product carousel")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "26735")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
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
