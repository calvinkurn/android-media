package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2022
 */
class PlayShortsAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayShortsAnalytic {

    /**
     * Mynakama Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
     */

    /**
     * Notes:
     * 1. Row 16 & 18 -> is it expected we use suffix "bottom sheet" while we are showing a popup dialog?
     * 2. Row 34 -> is it expected we just have label "click - simpan" without more context on what data is being saved?
     */

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) {
            SHORTS_CURRENT_SITE_SELLER
        } else {
            SHORTS_CURRENT_SITE_MAIN
        }

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    /** Row 10 */
    fun clickBackPostCreationEvent(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - back post creation",
            account = account,
            trackerId = "37533"
        )
    }

    /** Row 11 */
    fun clickCloseCoachMarkEvent(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - close coachmark",
            account = account,
            trackerId = "37534"
        )
    }

    /** Row 12 */
    fun clickAvailableAccountEvent(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - available account",
            account = account,
            trackerId = "37535"
        )
    }

    /** Row 13 */
    fun clickSwitchProfile(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - close switch profile",
            account = account,
            trackerId = "37536"
        )
    }

    /** Row 14 */
    fun clickUserAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - pilih akun user",
            account = account,
            trackerId = "37537"
        )
    }

    /** Row 15 */
    fun clickShopAccount(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - pilih akun shop",
            account = account,
            trackerId = "37538"
        )
    }

    /** Row 16 */
    fun viewSwitchAccountToShopConfirmation(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile shop bottom sheet",
            account = account,
            trackerId = "37539"
        )
    }

    /** Row 17 */
    fun clickCancelSwitchAccountToShop(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - batal switch to shop",
            account = account,
            trackerId = "37540"
        )
    }

    /** Row 18 */
    fun viewSwitchAccountToUserConfirmation(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile user bottom sheet",
            account = account,
            trackerId = "37541"
        )
    }

    /** Row 19 */
    fun clickCancelSwitchAccountToUser(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - batal switch to user",
            account = account,
            trackerId = "37542"
        )
    }

    /** Row 20 */
    fun clickCancelOnCompleteOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - x register user profile",
            account = account,
            trackerId = "37543"
        )
    }

    /** Row 21 */
    fun clickTypeUsernameOnCompleteOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - type user profile name",
            account = account,
            trackerId = "37544"
        )
    }

    /** Row 22 */
    fun clickAcceptTncOnCompleteOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - accept t&c",
            account = account,
            trackerId = "37545"
        )
    }

    /** Row 23 */
    fun viewCompleteOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - register user profile",
            account = account,
            trackerId = "37546"
        )
    }

    /** Row 24 */
    fun clickContinueOnCompleteOnboardingUGC(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut register user profile",
            account = account,
            trackerId = "37547"
        )
    }

    /** Row 25 */
    fun viewSwitchAccountBottomSheet(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - switch profile bottom sheet",
            account = account,
            trackerId = "37548"
        )
    }

    /** Row 28 */
    fun viewPreparationPage(account: ContentAccountUiModel) {
        sendGeneralViewEvent(
            eventAction = "view - post creation page",
            account = account,
            trackerId = "37551"
        )
    }

    /** Row 29 */
    fun clickMenuTitle(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - edit title",
            account = account,
            trackerId = "37552"
        )
    }

    /** Row 30 */
    fun clickProductMenu(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - add product tag",
            account = account,
            trackerId = "37553"
        )
    }

    /** Row 31 */
    fun clickCoverMenu(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - add cover on preparation page",
            account = account,
            trackerId = "37554"
        )
    }

    /** Row 32 */
    fun clickBackOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - back title page",
            account = account,
            trackerId = "37555"
        )
    }

    /** Row 33 */
    fun clickTextBoxOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - fill text title",
            account = account,
            trackerId = "37556"
        )
    }

    /** Row 34 */
    fun clickSaveOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - simpan",
            account = account,
            trackerId = "37557"
        )
    }

    /** Row 35 */
    fun clickClearTextBoxOnTitleForm(account: ContentAccountUiModel) {
        sendGeneralClickEvent(
            eventAction = "click - delete text title",
            account = account,
            trackerId = "37558"
        )
    }

    /** Row 36 */
    fun openScreenTitleForm(account: ContentAccountUiModel) {
        sendGeneralOpenScreen(
            screenName = "/play broadcast short - title page - ${getEventLabelByAccount(account)}",
            trackerId = "37559",
        )
    }

    private fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_OPEN_SCREEN)
                .setCustomProperty(IS_LOGGED_IN_STATUS_LABEL, userSession.isLoggedIn)
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
                .setCustomProperty(SCREEN_NAME_LABEL, screenName)
        )
    }

    private fun sendGeneralViewEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_VIEW_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(getEventLabelByAccount(account))
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    private fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_CLICK_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(getEventLabelByAccount(account))
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    private fun sendGeneralEvent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(SHORTS_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(SESSION_IRIS_LABEL, sessionIris)
            .build()
            .send()
    }

    private fun getEventLabelByAccount(account: ContentAccountUiModel): String {
        return "${account.id} - ${getAccountType(account)}"
    }

    private fun getAccountType(account: ContentAccountUiModel): String {
        return when {
            account.isShop -> SHORTS_TYPE_SHOP
            account.isUser -> SHORTS_TYPE_USER
            else -> ""
        }
    }

    companion object {
        private const val SHORTS_OPEN_SCREEN = "openScreen"
        private const val SHORTS_VIEW_CONTENT = "viewContentIris"
        private const val SHORTS_CLICK_CONTENT = "clickContent"
        private const val SHORTS_EVENT_CATEGORY = "play broadcast short"
        private const val SHORTS_BUSINESS_UNIT = "play"
        private const val SHORTS_TYPE_USER = "user"
        private const val SHORTS_TYPE_SHOP = "shop"
        private const val SHORTS_CURRENT_SITE_SELLER = "tokopediaseller"
        private const val SHORTS_CURRENT_SITE_MAIN = "tokopediamarketplace"

        private const val TRACKER_ID_LABEL = "trackerId"
        private const val SESSION_IRIS_LABEL = "sessionIris"
        private const val SCREEN_NAME_LABEL = "screenName"
        private const val IS_LOGGED_IN_STATUS_LABEL = "isLoggedInStatus"
    }
}
