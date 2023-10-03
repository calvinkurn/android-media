package com.tokopedia.play.broadcaster.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalytic
import com.tokopedia.play.broadcaster.analytic.entrypoint.PlayShortsEntryPointAnalytic
import com.tokopedia.play.broadcaster.analytic.interactive.PlayBroadcastInteractiveAnalytic
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.PlayBroSetupCoverAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.menu.PlayBroSetupMenuAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.title.PlayBroSetupTitleAnalytic
import com.tokopedia.play.broadcaster.analytic.summary.PlayBroadcastSummaryAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by mzennis on 13/07/20.
 * https://docs.google.com/spreadsheets/d/1efo8NsCI_ECqvd77pdy6F0J_053drVjmGbAb4hJOKlg/edit#gid=292707357
 * Row 8 - 69
 * Save Live to VOD https://mynakama.tokopedia.com/datatracker/product/requestdetail/161
 * Channel Scheduling https://mynakama.tokopedia.com/datatracker/product/requestdetail/247
 */
class PlayBroadcastAnalytic(
    private val userSession: UserSessionInterface,
    private val interactiveAnalytic: PlayBroadcastInteractiveAnalytic,
    private val setupMenuAnalytic: PlayBroSetupMenuAnalytic,
    private val setupTitleAnalytic: PlayBroSetupTitleAnalytic,
    private val setupCoverAnalytic: PlayBroSetupCoverAnalytic,
    private val setupProductAnalytic: PlayBroSetupProductAnalytic,
    private val summaryAnalytic: PlayBroadcastSummaryAnalytic,
    private val scheduleAnalytic: PlayBroScheduleAnalytic,
    private val pinProductAnalytic: PlayBroadcastPinProductAnalytic,
    private val accountAnalytic: PlayBroadcastAccountAnalytic,
    private val shortsEntryPointAnalytic: PlayShortsEntryPointAnalytic,
    private val playPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic,
    private val beautificationAnalytic: PlayBroadcastBeautificationAnalytic
) : PlayBroadcastInteractiveAnalytic by interactiveAnalytic,
    PlayBroSetupMenuAnalytic by setupMenuAnalytic,
    PlayBroSetupTitleAnalytic by setupTitleAnalytic,
    PlayBroSetupCoverAnalytic by setupCoverAnalytic,
    PlayBroSetupProductAnalytic by setupProductAnalytic,
    PlayBroadcastSummaryAnalytic by summaryAnalytic,
    PlayBroScheduleAnalytic by scheduleAnalytic,
    PlayBroadcastPinProductAnalytic by pinProductAnalytic,
    PlayBroadcastAccountAnalytic by accountAnalytic,
    PlayShortsEntryPointAnalytic by shortsEntryPointAnalytic,
    PlayPerformanceDashboardEntryPointAnalytic by playPerformanceDashboardEntryPointAnalytic,
    PlayBroadcastBeautificationAnalytic by beautificationAnalytic {

    /**
     * View Camera and Microphone Permission Page
     */
    fun openPermissionScreen() {
        sendScreen("/$KEY_TRACK_CATEGORY - permission page - ${userSession.shopId}")
    }

    /**
     * View On Boarding Page
     */
    fun openSetupScreen() {
        sendScreen("/$KEY_TRACK_CATEGORY - onboarding page - ${userSession.shopId}")
    }

    /**
     * Click `X` (exit from On Boarding Page)
     */
    fun clickCloseOnSetupPage() {
        clickGeneralEvent(
            action = "close onboarding"
        )
    }

    /**
     * View Error Message (:Param) on Preparation Page
     */
    fun viewErrorOnFinalSetupPage(errorMessage: String) {
        viewCustomGeneralEvent(
            action = "error state on preparation page",
            label = "- $errorMessage"
        )
    }

    /**
     * View Live Room Page
     */
    fun openBroadcastScreen(channelId: String) {
        sendScreen("/$KEY_TRACK_CATEGORY - live room - ${userSession.shopId} - $channelId")
    }

    /**
     * Click Product Tag on Live Room
     */
    fun clickProductTagOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "product tag live",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Share Link on Live Room
     */
    fun clickShareIconOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "share link live",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Camera Switch
     */
    fun clickSwitchCameraOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "camera switch live",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Add Pin Chat Message
     */
    fun clickAddPinChatMessage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "add pin chat message",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Edit Pin Chat Message
     */
    fun clickEditPinChatMessage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "edit pin chat message",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Save Pin Chat Message
     */
    fun clickSavePinChatMessage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "save pin chat message",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Scroll Product Tag Carousel
     */
    fun scrollProductTag(channelId: String, product: ProductUiModel, position: Int) {
        scrollGeneralEvent(
            "- product tag carousel",
            "- $channelId - ${product.id} - $position"
        )
    }

    /**
     * View Exit Pop Up on Live Room
     */
    fun viewDialogExitOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
            "popup message exit",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click Keluar on Exit Pop Up
     */
    fun clickDialogExitOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "keluar live room",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * View Pop Up `Lihat Laporan` on Live Room
     */
    fun viewDialogSeeReportOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
            "lihat laporan message",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click `Lihat Laporan` on Live Room
     */
    fun clickDialogSeeReportOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "lihat laporan",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * View Error Message (:Param) on Live Room
     */
    fun viewErrorOnLivePage(channelId: String, titleChannel: String, errorMessage: String) {
        viewCustomGeneralEvent(
            "error state on live room",
            "- $channelId - $titleChannel - $errorMessage"
        )
    }

    /**
     * View Resume Pop Up
     */
    fun viewDialogContinueBroadcastOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
            "lanjutkan siaran message",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * Click `Lanjutkan` to continue the streaming on Live Room
     */
    fun clickDialogContinueBroadcastOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
            "lanjutkan siaran",
            "- $channelId - $titleChannel"
        )
    }

    /**
     * View Multiple Device Violation Pop Up
     */
    fun viewDialogViolation(channelId: String) {
        viewGeneralEvent(
            "device violation popup message",
            "- $channelId"
        )
    }

    /**
     * View Error Message on Report Page
     */
    fun viewErrorOnReportPage(channelId: String, titleChannel: String, errorMessage: String) {
        viewCustomGeneralEvent(
            "error state on report page",
            "- $channelId - $titleChannel - $errorMessage"
        )
    }

    /**
     * Seller click save channel schedule
     */
    fun clickSaveScheduleOnFinalSetupPage() {
        clickGeneralEvent(
            "simpan jadwal"
        )
    }

    private fun viewGeneralEvent(action: String, label: String = "") {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_VIEW)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        viewCustomGeneralEvent(
            action = eventAction.toString(),
            label = label
        )
    }

    private fun viewCustomGeneralEvent(action: String, label: String = "") {
        sendGeneralEvent(
            event = KEY_TRACK_VIEW_EVENT,
            action = action,
            label = label
        )
    }

    private fun clickGeneralEvent(action: String, label: String = "") {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_CLICK)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        sendGeneralEvent(
            event = KEY_TRACK_CLICK_EVENT,
            action = eventAction.toString(),
            label = label
        )
    }

    private fun impressionGeneralEvent(action: String, label: String = "") {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_IMPRESSION)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        sendGeneralEvent(
            event = KEY_TRACK_VIEW_EVENT,
            action = eventAction.toString(),
            label = label
        )
    }

    private fun scrollGeneralEvent(action: String, label: String = "") {
        val eventAction = StringBuilder()
        eventAction.append(KEY_TRACK_SCROLL)
        if (action.isNotBlank()) {
            eventAction.append(" ")
            eventAction.append(action)
        }

        sendGeneralEvent(
            event = KEY_TRACK_CLICK_EVENT,
            action = eventAction.toString(),
            label = label
        )
    }

    private fun sendGeneralEvent(event: String, action: String, label: String) {
        val eventLabel = StringBuilder()
        eventLabel.append(userSession.shopId)
        if (label.isNotBlank()) {
            eventLabel.append(" ")
            eventLabel.append(label)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to event,
                Key.eventCategory to KEY_TRACK_CATEGORY,
                Key.eventAction to action,
                Key.eventLabel to eventLabel,
                Key.currentSite to currentSite,
                Key.shopId to userSession.shopId,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    private fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            hashMapOf(
                Key.currentSite to currentSite,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }
}
