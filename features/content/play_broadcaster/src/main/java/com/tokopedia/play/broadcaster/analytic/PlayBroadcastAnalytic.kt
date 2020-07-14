package com.tokopedia.play.broadcaster.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface


/**
 * Created by mzennis on 13/07/20.
 * https://docs.google.com/spreadsheets/d/1efo8NsCI_ECqvd77pdy6F0J_053drVjmGbAb4hJOKlg/edit#gid=292707357
 * Row 8 - 69
 */
class PlayBroadcastAnalytic(private val userSession: UserSessionInterface) {

    fun openPermissionScreen() {
        sendScreen("/$KEY_TRACK_CATEGORY - permission page - ${userSession.shopId}")
    }

    fun openSetupScreen() {
        sendScreen("/$KEY_TRACK_CATEGORY - onboarding page - ${userSession.shopId}")
    }

    fun clickSwitchCamera() {
        clickGeneralEvent(
                action = "camera onboarding"
        )
    }

    fun clickTnC() {
        clickGeneralEvent(
                action = "syarat dan ketentuan"
        )
    }

    fun viewTnC() {
        viewGeneralEvent(
                action = "syarat dan ketentuan"
        )
    }

    fun clickPrepareBroadcast() {
        clickGeneralEvent(
                action = "mulai persiapannya"
        )
    }

    fun clickClosePrepareBroadcast() {
        clickGeneralEvent(
                action = "close onboarding"
        )
    }

    fun viewProductBottomSheet() {
        viewGeneralEvent(
                action = "product tagging bottomsheet"
        )
    }

    fun clickSearchBarBottomSheet(query: String) {
        clickGeneralEvent(
                action = "search bar",
                label = "- $query"
        )
    }

    fun clickEtalaseBottomSheet(etalaseName: String) {
        clickGeneralEvent(
                action = "etalase card",
                label = "- $etalaseName"
        )
    }

    fun clickProductBottomSheet(productId: String, selected: Boolean) {
        val checkOrUncheck = if (selected) "check" else "uncheck"
        clickGeneralEvent(
                action = "product card",
                label = "- $checkOrUncheck - $productId"
        )
    }

    fun clickSelectedProductBottomSheet() {
        clickGeneralEvent(
                action = "selected product icon"
        )
    }

    fun clickContinueProductBottomSheet() {
        clickGeneralEvent(
                action = "lanjutkan on product tag"
        )
    }

    fun viewProductSearchBottomSheet() {
        viewGeneralEvent(
                action = "search bar"
        )
    }

    fun clickSearchBarResultBottomSheet(query: String, productId: String) {
        clickGeneralEvent(
                action = "product name suggested",
                label = "- $query - $productId"
        )
    }

    fun viewErrorEtalaseBottomSheet(errorMessage: String) {
        viewCustomGeneralEvent(
                action = "error state on etalase",
                label = "- $errorMessage"
        )
    }

    fun viewErrorProductBottomSheet(errorMessage: String) {
        viewCustomGeneralEvent(
                action = "error state on product",
                label = "- $errorMessage"
        )
    }

    fun viewCoverTitleBottomSheet() {
        viewGeneralEvent(
                action = "add cover and title"
        )
    }

    fun clickAddCoverBottomSheet() {
        clickGeneralEvent(
                action = "add cover"
        )
    }

    fun clickAddTitleBottomSheet() {
        clickGeneralEvent(
                action = "add title"
        )
    }

    fun viewAddCoverSourceBottomSheet() {
        viewGeneralEvent(
                action = "add cover source bottom sheet"
        )
    }

    fun clickAddCoverFromCameraSourceBottomSheet() {
        clickGeneralEvent(
                action = "camera source"
        )
    }

    fun clickAddCoverFromPdpSourceBottomSheet() {
        clickGeneralEvent(
                action = "pdp photo source"
        )
    }

    fun clickAddCoverFromGalleryBottomSheet() {
        clickGeneralEvent(
                action = "internal gallery source"
        )
    }

    fun openCameraScreenToAddCover() {
        sendScreen("/$KEY_TRACK_CATEGORY - camera - ${userSession.shopId}")
    }

    fun clickCaptureFromCameraToAddCover() {
        clickGeneralEvent(
                action = "capture"
        )
    }

    fun clickSwitchCameraOnAddCover() {
        clickGeneralEvent(
                action = "switch camera on add cover"
        )
    }

    fun clickTimerCameraOnAddCover(second: Long) {
        clickGeneralEvent(
                action = "timer",
                label = "- $second"
        )
    }

    fun clickCancelCameraOnAddCover() {
        clickGeneralEvent(
                action = "cancel"
        )
    }

    fun viewAddCoverCroppingBottomSheet() {
        viewGeneralEvent(
                action = "cropping page"
        )
    }

    fun clickChangeCoverBottomSheet() {
        clickGeneralEvent(
                action = "ganti"
        )
    }

    fun clickContinueOnCroppingCoverBottomSheet() {
        clickGeneralEvent(
                action = "lanjutkan on cropping page"
        )
    }

    fun clickContinueOnCoverBottomSheet() {
        clickGeneralEvent(
                action = "lanjutkan on add cover"
        )
    }

    fun openFinalSetupScreen() {
        sendScreen("") // it is empty, from the doc row 41
    }

    fun clickEditTitleOnFinalSetup() {
        clickGeneralEvent(
                action = "title live streaming"
        )
    }

    fun clickEditProductTaggingOnFinalSetup() {
        clickGeneralEvent(
                action = "edit product tagging"
        )
    }

    fun clickEditCoverOnFinalSetup() {
        clickGeneralEvent(
                action = "edit cover"
        )
    }

    fun clickShareIconOnFinalSetup() {
        clickGeneralEvent(
                action = "share link on preparation"
        )
    }

    fun clickSwitchCameraOnFinalSetup() {
        clickGeneralEvent(
                action = "switch camera on preparation page"
        )
    }

    fun clickStartStreamingOnFinalSetup() {
        clickGeneralEvent(
                action = "mulai live streaming"
        )
    }

    fun clickSubmitEditTitleOnFinalSetup() {
        clickGeneralEvent(
                action = "simpan"
        )
    }

    fun clickChooseOverProductOnFinalSetup() {
        clickGeneralEvent(
                action = "pilih ulang product"
        )
    }

    fun clickSubmitProductOnFinalSetup() {
        clickGeneralEvent(
                action = "simpan on product tag"
        )
    }

    fun viewDialogExitOnFinalSetup() {
        viewGeneralEvent(
                action = "popup message exit on preparation page"
        )
    }

    fun clickDialogExitOnFinalSetup() {
        clickGeneralEvent(
                action = "keluar on preparation page"
        )
    }

    fun viewErrorOnFinalSetup(errorMessage: String) {
        viewGeneralEvent(
                action = "error state on preparation page",
                label = " - $errorMessage"
        )
    }

    fun openBroadcastScreen(channelId: String) {
        sendScreen("/$KEY_TRACK_CATEGORY - live room - ${userSession.shopId} - $channelId")
    }

    fun clickProductTagOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "product tag live",
                "- $channelId - $titleChannel"
        )
    }

    fun clickShareIconOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "share link live",
                "- $channelId - $titleChannel"
        )
    }

    fun clickSwitchCameraOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "camera switch live",
                "- $channelId - $titleChannel"
        )
    }

    fun viewDialogExitOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
                "popup message exit",
                "- $channelId - $titleChannel"
        )
    }

    fun clickDialogExitOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "keluar live room",
                "- $channelId - $titleChannel"
        )
    }

    fun viewDialogSeeReportOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
                "lihat laporan message",
                "- $channelId - $titleChannel"
        )
    }

    fun clickDialogSeeReportOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "lihat laporan",
                "- $channelId - $titleChannel"
        )
    }

    fun viewErrorOnLivePage(channelId: String, titleChannel: String) {
        viewCustomGeneralEvent(
                "error state on live room",
                "- $channelId - $titleChannel"
        )
    }

    fun viewDialogContinueBroadcastOnLivePage(channelId: String, titleChannel: String) {
        viewGeneralEvent(
                "lanjutkan siaran message",
                "- $channelId - $titleChannel"
        )
    }

    fun clickDialogContinueBroadcastOnLivePage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "lanjutkan siaran",
                "- $channelId - $titleChannel"
        )
    }

    fun viewDialogViolation(channelId: String, titleChannel: String) {
        viewGeneralEvent(
                "device violation popup message",
                "- $channelId - $titleChannel"
        )
    }

    fun openReportScreen(channelId: String) {
        sendScreen("/$KEY_TRACK_CATEGORY - report summary - ${userSession.shopId} - $channelId")
    }

    fun clickDoneOnReportPage(channelId: String, titleChannel: String) {
        clickGeneralEvent(
                "selesai on report page",
                "- $channelId - $titleChannel"
        )
    }

    fun viewErrorOnReportPage(channelId: String, titleChannel: String, errorMessage: String) {
        viewCustomGeneralEvent(
                "error state on report page",
                "- $errorMessage - $channelId - $titleChannel"
        )
    }

    /**
     *
     */
    private fun viewGeneralEvent(action: String, label: String = "") {
        val eventAction = KEY_TRACK_VIEW
        if (action.isNotBlank()) {
            eventAction.plus(" ")
            eventAction.plus(action)
        }

        viewCustomGeneralEvent(
                action = eventAction,
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

    private fun clickGeneralEvent(action: String, label: String =  "") {
        val eventAction = KEY_TRACK_CLICK
        if (action.isNotBlank()) {
            eventAction.plus(" ")
            eventAction.plus(action)
        }

        sendGeneralEvent(
                event = KEY_TRACK_CLICK_EVENT,
                action = eventAction,
                label = label
        )
    }

    private fun sendGeneralEvent(event: String, action: String, label: String) {
        val eventLabel = userSession.shopId
        if (label.isNotBlank()) {
            eventLabel.plus(" ")
            eventLabel.plus(label)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to event,
                        KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                        KEY_EVENT_ACTION to action,
                        KEY_EVENT_LABEL to eventLabel,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SHOP_ID to userSession.shopId,
                        KEY_USER_ID to userSession.userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    private fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
                screenName,
                hashMapOf(
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    companion object {
        private const val KEY_EVENT = "event"

        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"

        private const val KEY_USER_ID = "userId"
        private const val KEY_SHOP_ID = "shopId"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

        private const val KEY_TRACK_CURRENT_SITE = "tokopediaseller"
        private const val KEY_TRACK_BUSINESS_UNIT = "play"
        private const val KEY_TRACK_CATEGORY = "seller broadcast"

        private const val KEY_TRACK_CLICK_EVENT = "clickSellerBroadcast"
        private const val KEY_TRACK_VIEW_EVENT = "viewSellerBroadcastIris"

        private const val KEY_TRACK_CLICK = "click"
        private const val KEY_TRACK_VIEW = "view"
    }
}