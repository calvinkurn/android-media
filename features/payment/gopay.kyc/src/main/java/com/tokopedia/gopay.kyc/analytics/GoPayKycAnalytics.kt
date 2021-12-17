package com.tokopedia.gopay.kyc.analytics

import com.tokopedia.gopay.kyc.di.GoPayKycScope
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GoPayKycScope
class GoPayKycAnalytics @Inject constructor(
    val userSession: dagger.Lazy<UserSessionInterface>
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sentKycEvent(event: GoPayKycEvent) {
        when(event) {
            is GoPayKycEvent.Impression.OpenScreenEvent -> sendOpenScreenEvent(event.pageSource)
            is GoPayKycEvent.Impression.KycFailedImpression -> sendKycFailedImpression()
            is GoPayKycEvent.Click.BackPressEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_BACK_KYC, event.pageSource)
            is GoPayKycEvent.Click.UpgradeKycEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_UPGRADE_KYC, event.pageSource)
            is GoPayKycEvent.Click.TakePhotoEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_TAKE_PHOTO, event.eventLabel)
            is GoPayKycEvent.Click.ReTakePhotoEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_TAKE_PHOTO_AGAIN, event.eventLabel)
            is GoPayKycEvent.Click.ConfirmPhotoEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_USE_PHOTO, event.eventLabel)
            is GoPayKycEvent.Click.ConfirmOkDialogEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_PROCEED_UPGRADE,
                    GoPayKycConstants.Label.GOPAY_UPGRADE)
            is GoPayKycEvent.Click.ExitKycDialogEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_EXIT_UPGRADE,
                    GoPayKycConstants.Label.GOPAY_UPGRADE_QUIT)
            is GoPayKycEvent.Click.UploadKycEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_SUBMIT_KYC, "")
            is GoPayKycEvent.Click.TncEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_TNC_SUMMARY_PAGE, "")
            is GoPayKycEvent.Click.SubmitOkEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_OK_SUCCESS, "")
            is GoPayKycEvent.Click.RetrySubmitEvent ->
                sendActionClickEvents(GoPayKycConstants.Action.CLICK_RETRY_AGAIN, "")

        }
    }

    fun sendOpenScreenEvent(pageSource: String) {
        analyticTracker.sendScreenAuthenticated(GoPayKycConstants.ScreenNames.GOPAY_DASHBOARD,
            mutableMapOf(
                GoPayKycConstants.KEY_BUSINESS_UNIT to GoPayKycConstants.VALUE_BUSINESS_UNIT,
                GoPayKycConstants.KEY_CURRENT_SITE to GoPayKycConstants.VALUE_CURRENT_SITE,
                GoPayKycConstants.PAGE_SOURCE to pageSource
            )
        )
    }

    private fun sendKycFailedImpression() {
        val map = TrackAppUtils.gtmData(
            GoPayKycConstants.Event.VIEW_IMPRESSION,
            GoPayKycConstants.Category.PEMUDA_KYC_PAGE,
            GoPayKycConstants.Action.IMPRESSION_UPLOAD_FAILED_BOTTOMSHEET,
            ""
        )
        sendGeneralEvent(map)
    }


    private fun sendActionClickEvents(action: String, label: String) {
        if (action.isEmpty()) return
        val map = TrackAppUtils.gtmData(
            GoPayKycConstants.Event.CLICK_PAYMENT,
            GoPayKycConstants.Category.PEMUDA_KYC_PAGE,
            action,
            label
        )
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[GoPayKycConstants.KEY_BUSINESS_UNIT] = GoPayKycConstants.VALUE_BUSINESS_UNIT
        map[GoPayKycConstants.KEY_CURRENT_SITE] = GoPayKycConstants.VALUE_CURRENT_SITE
        analyticTracker.sendGeneralEvent(map)
    }

}