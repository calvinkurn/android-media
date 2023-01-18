package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2022
 */
class PlayShortsAnalyticImpl @Inject constructor(
    private val analyticSender: PlayShortsAnalyticSender,
) : PlayShortsAnalytic {

    /**
     * Mynakama Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
     */

    /**
     * Notes:
     * 1. Row 16 & 18 -> is it expected we use suffix "bottom sheet" while we are showing a popup dialog?
     * 2. Row 34 -> is it expected we just have label "click - simpan" without more context on what data is being saved?
     * 3. Row 53 - 56 -> its not applicable since android still using old cover picker
     * 4. Row 58 -> we dont have "Simpan" button in existing feature
     * 5. Row 59 & 60 -> is it expected we use suffix "bottom sheet" while we are showing a popup dialog?
     * 6. Row 20 - 24 -> theres a possiblity where eventLabel is " - " bcs theres no selected account, so how?
     */

    /** Row 10 */
    override fun clickBackOnPreparationPage(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - back post creation",
            account = account,
            trackerId = getTrackerIdBySite("37533", "37615")
        )
    }

    /** Row 11 */
    override fun clickCloseCoachMarkOnPreparationPage(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - close coachmark",
            account = account,
            trackerId = getTrackerIdBySite("37534", "37616")
        )
    }

    /** Row 12 */
    override fun clickSwitchAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - available account",
            account = account,
            trackerId = "37535"
        )
    }

    /** Row 13 */
    override fun clickCloseSwitchAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - close switch profile",
            account = account,
            trackerId = "37536"
        )
    }

    /** Row 14 */
    override fun clickUserAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - pilih akun user",
            account = account,
            trackerId = "37537"
        )
    }

    /** Row 15 */
    override fun clickShopAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - pilih akun shop",
            account = account,
            trackerId = "37538"
        )
    }

    /** Row 16 */
    override fun viewSwitchAccountToShopConfirmation(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile shop bottom sheet",
            account = account,
            trackerId = "37539"
        )
    }

    /** Row 17 */
    override fun clickCancelSwitchAccountToShop(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - batal switch to shop",
            account = account,
            trackerId = "37540"
        )
    }

    /** Row 18 */
    override fun viewSwitchAccountToUserConfirmation(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile user bottom sheet",
            account = account,
            trackerId = "37541"
        )
    }

    /** Row 19 */
    override fun clickCancelSwitchAccountToUser(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - batal switch to user",
            account = account,
            trackerId = "37542"
        )
    }

    /** Row 20 */
    override fun clickCancelOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - x register user profile",
            account = account,
            trackerId = "37543"
        )
    }

    /** Row 21 */
    override fun clickTextFieldUsernameOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - type user profile name",
            account = account,
            trackerId = "37544"
        )
    }

    /** Row 22 */
    override fun clickAcceptTncOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - accept t&c",
            account = account,
            trackerId = "37545"
        )
    }

    /** Row 23 */
    override fun viewOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - register user profile",
            account = account,
            trackerId = "37546"
        )
    }

    /** Row 24 */
    override fun clickContinueOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut register user profile",
            account = account,
            trackerId = "37547"
        )
    }

    /** Row 25 */
    override fun viewSwitchAccountBottomSheet(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile bottom sheet",
            account = account,
            trackerId = "37548"
        )
    }

    /** Row 28 */
    override fun viewPreparationPage(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - post creation page",
            account = account,
            trackerId = "37551"
        )
    }

    /** Row 29 */
    override fun clickMenuTitle(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - edit title",
            account = account,
            trackerId = "37552"
        )
    }

    /** Row 30 */
    override fun clickMenuProduct(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - add product tag",
            account = account,
            trackerId = "37553"
        )
    }

    /** Row 31 */
    override fun clickMenuCover(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - add cover on preparation page",
            account = account,
            trackerId = "37554"
        )
    }

    /** Row 32 */
    override fun clickBackOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - back title page",
            account = account,
            trackerId = getTrackerIdBySite("37555", "37637")
        )
    }

    /** Row 33 */
    override fun clickTextFieldOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - fill text title",
            account = account,
            trackerId = getTrackerIdBySite("37556", "37638")
        )
    }

    /** Row 34 */
    override fun clickSaveOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - simpan",
            account = account,
            trackerId = getTrackerIdBySite("37557", "37639")
        )
    }

    /** Row 35 */
    override fun clickClearTextBoxOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - delete text title",
            account = account,
            trackerId = getTrackerIdBySite("37558", "37640")
        )
    }

    /** Row 36 */
    override fun openScreenTitleForm(account: ContentAccountUiModel) {
        sendGeneralOpenScreen(
            screenName = "/play broadcast short - title page - ${PlayShortsAnalyticHelper.getEventLabelByAccount(account)}",
            trackerId = getTrackerIdBySite("37559", "37641")
        )
    }

    /** Row 51 */
    override fun clickCloseOnCoverForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - close cover page",
            account = account,
            trackerId = "37574"
        )
    }

    /** Row 52 */
    override fun clickSelectCoverOnCoverForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - edit cover",
            account = account,
            trackerId = "37575"
        )
    }

    /** Row 57 */
    override fun openScreenCoverForm(account: ContentAccountUiModel) {
        sendGeneralOpenScreen(
            screenName = "/play broadcast short - cover page - ${PlayShortsAnalyticHelper.getEventLabelByAccount(account)}",
            trackerId = "37580"
        )
    }

    /** Row 59 */
    override fun viewLeavePreparationConfirmationPopup(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - yakin mau keluar botom sheet",
            account = account,
            trackerId = "37582"
        )
    }

    /** Row 60 */
    override fun clickContinueOnLeaveConfirmationPopup(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut persiapan bottom sheet",
            account = account,
            trackerId = "37583"
        )
    }

    /** Row 61 */
    override fun clickBackOnSummaryPage(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - back summary page",
            account = account,
            trackerId = "37584"
        )
    }

    /** Row 62 */
    override fun clickContentTag(tag: String, account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - content tag",
            eventLabel = "${account.id} - $tag - ${PlayShortsAnalyticHelper.getAccountType(account)}",
            trackerId = "37585"
        )
    }

    /** Row 63 */
    override fun clickUploadVideo(channelId: String, account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - upload video",
            eventLabel = "${PlayShortsAnalyticHelper.getEventLabelByAccount(account)} - $channelId",
            trackerId = "37586"
        )
    }

    /** Row 65 */
    override fun openScreenSummaryPage(account: ContentAccountUiModel) {
        sendGeneralOpenScreen(
            screenName = "/play broadcast short - summary page - ${PlayShortsAnalyticHelper.getEventLabelByAccount(account)}",
            trackerId = "37588",
        )
    }

    /** Row 67 */
    override fun clickRefreshContentTag(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - refresh content tags",
            account = account,
            trackerId = "37590"
        )
    }

    /** Row 82 */
    override fun clickNextOnPreparationPage(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut post creation",
            account = account,
            trackerId = "37605"
        )
    }

    private fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return PlayShortsAnalyticHelper.getTrackerIdBySite(mainAppTrackerId, sellerAppTrackerId)
    }

    private fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    ) {
        analyticSender.sendGeneralOpenScreen(screenName, trackerId)
    }

    private fun sendGeneralViewEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        analyticSender.sendGeneralViewEvent(eventAction, account, trackerId)
    }

    private fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        analyticSender.sendGeneralClickEvent(eventAction, account, trackerId)
    }

    private fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        analyticSender.sendGeneralClickEvent(eventAction, eventLabel, trackerId)
    }
}
