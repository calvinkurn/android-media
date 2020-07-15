package com.tokopedia.play.broadcaster.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface


/**
 * Created by mzennis on 13/07/20.
 * https://docs.google.com/spreadsheets/d/1efo8NsCI_ECqvd77pdy6F0J_053drVjmGbAb4hJOKlg/edit#gid=292707357
 * Row 8 - 69
 */
class PlayBroadcastAnalytic(private val userSession: UserSessionInterface) {

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
     * Click Camera Switch on On Boarding Page
     */
    fun clickSwitchCameraOnSetupPage() {
        clickGeneralEvent(
                action = "camera onboarding"
        )
    }

    /**
     * Click Terms and Condition Ticker
     */
    fun clickTnC() {
        clickGeneralEvent(
                action = "syarat dan ketentuan"
        )
    }

    /**
     * View Terms and Condition Ticker
     */
    fun viewTnC() {
        viewGeneralEvent(
                action = "syarat dan ketentuan"
        )
    }

    /**
     * Click `Mulai persiapannya` on Onboarding Page
     */
    fun clickPrepareBroadcast() {
        clickGeneralEvent(
                action = "mulai persiapannya"
        )
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
     * View Product Tagging Bottomsheet
     */
    fun viewProductBottomSheet() {
        viewGeneralEvent(
                action = "product tagging bottomsheet"
        )
    }

    /**
     * Click Search Bar
     */
    fun clickSearchBar(query: String) {
        clickGeneralEvent(
                action = "search bar",
                label = "- $query"
        )
    }

    /**
     * Click Etalase Card
     */
    fun clickEtalase(etalaseName: String) {
        clickGeneralEvent(
                action = "etalase card",
                label = "- $etalaseName"
        )
    }

    /**
     * Click Product Card (Check - Uncheck) - (Etalase - Search - Selected)
     */
    fun clickProductCard(productId: String, selected: Boolean) {
        val checkOrUncheck = if (selected) "check" else "uncheck"
        clickGeneralEvent(
                action = "product card",
                label = "- $checkOrUncheck - $productId"
        )
    }

    /**
     * Click Selected Product Icon (Etalase - Search)
     */
    fun clickSelectedProductIcon() {
        clickGeneralEvent(
                action = "selected product icon"
        )
    }

    /**
     * Click `Lanjutkan` on Product Tagging
     */
    fun clickContinueOnProductBottomSheet() {
        clickGeneralEvent(
                action = "lanjutkan on product tag"
        )
    }

    /**
     * View Search Page
     */
    fun viewSearchProductResult() {
        viewGeneralEvent(
                action = "search bar"
        )
    }

    /**
     * Click Product Name Suggestion
     */
    fun clickProductNameSuggestion(query: String, productId: String) {
        clickGeneralEvent(
                action = "product name suggested",
                label = "- $query - $productId"
        )
    }

    /**
     * View Error Message (Fail to fetch data) - Etalase
     */
    fun viewEtalaseError(errorMessage: String) {
        viewCustomGeneralEvent(
                action = "error state on etalase",
                label = "- $errorMessage"
        )
    }

    /**
     * View Error Message (Fail to fetch data) - Products
     */
    fun viewErrorProduct(errorMessage: String) {
        viewCustomGeneralEvent(
                action = "error state on product",
                label = "- $errorMessage"
        )
    }

    /**
     * View Add Cover and Title Bottomsheet
     */
    fun viewAddCoverTitleBottomSheet() {
        viewGeneralEvent(
                action = "add cover and title"
        )
    }

    /**
     * Click Add Cover
     */
    fun clickAddCover() {
        clickGeneralEvent(
                action = "add cover"
        )
    }

    /**
     * Click Add Title
     */
    fun clickAddTitle() {
        clickGeneralEvent(
                action = "add title"
        )
    }

    /**
     * View Add Cover Source Bottomsheet
     */
    fun viewAddCoverSourceBottomSheet() {
        viewGeneralEvent(
                action = "add cover source bottom sheet"
        )
    }

    /**
     * Click Camera Source
     */
    fun clickAddCoverFromCameraSource() {
        clickGeneralEvent(
                action = "camera source"
        )
    }

    /**
     * Click PDP Image Source
     */
    fun clickAddCoverFromPdpSource() {
        clickGeneralEvent(
                action = "pdp photo source"
        )
    }

    /**
     * Click Internal Gallery Source
     */
    fun clickAddCoverFromGallerySource() {
        clickGeneralEvent(
                action = "internal gallery source"
        )
    }

    /**
     * View Camera Page
     */
    fun openCameraScreenToAddCover() {
        sendScreen("/$KEY_TRACK_CATEGORY - camera - ${userSession.shopId}")
    }

    /**
     * Click Capture
     */
    fun clickCaptureFromCameraPage() {
        clickGeneralEvent(
                action = "capture"
        )
    }

    /**
     * Click Switch Camera
     */
    fun clickSwitchCameraOnCameraPage() {
        clickGeneralEvent(
                action = "switch camera on add cover"
        )
    }

    /**
     * Click Timer
     */
    fun clickTimerCameraOnCameraPage(second: Int) {
        clickGeneralEvent(
                action = "timer",
                label = "- $second"
        )
    }

    /**
     * Click Cancel
     */
    fun clickCancelOnCameraPage() {
        clickGeneralEvent(
                action = "cancel"
        )
    }

    /**
     * View Cropping Page
     */
    fun viewCroppingPage() {
        viewGeneralEvent(
                action = "cropping page"
        )
    }

    /**
     * Click `Ganti` on Cropping Page
     */
    fun clickChangeCoverOnCroppingPage() {
        clickGeneralEvent(
                action = "ganti"
        )
    }

    /**
     * Click `Lanjutkan` on Cropping Page
     */
    fun clickContinueOnCroppingPage() {
        clickGeneralEvent(
                action = "lanjutkan on cropping page"
        )
    }

    /**
     * Click `Lanjutkan` on Add Cover and Title
     */
    fun clickContinueOnAddCoverAndTitlePage() {
        clickGeneralEvent(
                action = "lanjutkan on add cover"
        )
    }

    /**
     * View Preparation Page
     */
    fun openFinalSetupPage() {
        sendScreen("") // it is empty, from the doc row 41
    }

    /**
     * Click Channel Title on Preparation Page
     */
    fun clickEditTitleOnFinalSetupPage() {
        clickGeneralEvent(
                action = "title live streaming"
        )
    }

    /**
     * Click Edit Product Tag/Pencil Icon on Preparation Page
     */
    fun clickEditProductTaggingOnFinalSetupPage() {
        clickGeneralEvent(
                action = "edit product tagging"
        )
    }

    /**
     * Click Edit Cover on Preparation Page Cover Edit
     */
    fun clickEditCoverOnFinalSetupPage() {
        clickGeneralEvent(
                action = "edit cover"
        )
    }

    /**
     * Click Share Link on Preparation Page
     */
    fun clickShareIconOnFinalSetupPage() {
        clickGeneralEvent(
                action = "share link on preparation"
        )
    }

    /**
     * Click Camera Switch on Preparation Page
     */
    fun clickSwitchCameraOnFinalSetupPage() {
        clickGeneralEvent(
                action = "switch camera on preparation page"
        )
    }

    /**
     * Click `Mulai Live Streaming` on Preparation Page
     */
    fun clickStartStreamingOnFinalSetupPage() {
        clickGeneralEvent(
                action = "mulai live streaming"
        )
    }

    /**
     * Click `Simpan` on Preparation Page Title Edit
     */
    fun clickSubmitOnEditTitleBottomSheet() {
        clickGeneralEvent(
                action = "simpan"
        )
    }

    /**
     * Click `Pilih ulang` on Preparation Page Product Tagging Edit Bottomsheet
     */
    fun clickChooseOverOnEditProductBottomSheet() {
        clickGeneralEvent(
                action = "pilih ulang product"
        )
    }

    /**
     * Click `Simpan` on Preparation Page Product Tagging Edit Bottomsheet
     */
    fun clickSubmitOnEditProductBottomSheet() {
        clickGeneralEvent(
                action = "simpan on product tag"
        )
    }

    /**
     * View Exit Modal on Preparation Page
     */
    fun viewExitDialogOnFinalSetupPage() {
        viewGeneralEvent(
                action = "popup message exit on preparation page"
        )
    }

    /**
     * Click `Keluar` on Exit Modal Preparation Page
     */
    fun clickExitOnDialogFinalSetupPage() {
        clickGeneralEvent(
                action = "keluar on preparation page"
        )
    }

    /**
     * View Error Message (:Param) on Preparation Page
     */
    fun viewErrorOnFinalSetupPage(errorMessage: String) {
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