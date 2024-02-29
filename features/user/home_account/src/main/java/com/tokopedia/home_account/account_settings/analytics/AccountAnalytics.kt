package com.tokopedia.home_account.account_settings.analytics

import android.content.Context
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import com.tokopedia.home_account.account_settings.AccountConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meta on 04/08/18.
 *
 *
 * Setting PIN : https://docs.google.com/spreadsheets/d/1H3CSARG5QtVACiffBxd2HJE7adKzAZ3xxmtEbR01Eho/edit?ts=5ca30084#gid=1785281730
 */
class AccountAnalytics @Inject constructor(private val userSessionInterface: UserSessionInterface) {
    fun homepageSaldoClick(context: Context?, landingScreen: String) {
        val map: MutableMap<String, Any> = HashMap()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
        TrackAnalytics.sendEvent(FirebaseEvent.Home.HAMBURGER_SALDO, map, context)
    }

    fun eventClickAccountSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", AccountConstants.Analytics.ACCOUNT, AccountConstants.Analytics.SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
            )
        )
    }

    fun eventClickAccountPassword() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s - %s", AccountConstants.Analytics.ACCOUNT,
                    AccountConstants.Analytics.SETTING,
                    AccountConstants.Analytics.PASSWORD),
                AccountConstants.Analytics.CLICK_ON_PASSWORD,
                ""
            )
        )
    }

    /* Tracker no.6 */
    fun eventClickFingerprint() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val data = TrackAppUtils.gtmData(
            AccountConstants.Analytics.CLICK_ACCOUNT_SETTING,
            String.format("%s %s", AccountConstants.Analytics.ACCOUNT,
                AccountConstants.Analytics.SETTING),
            AccountConstants.Analytics.CLICK_BIOMETRIC_BUTTON,
            String.format("%s - %s", AccountConstants.Analytics.CLICK,
                AccountConstants.Analytics.FINGERPRINT)
        )
        data[AccountConstants.Analytics.BUSINESS_UNIT] = AccountConstants.Analytics.USER_PLATFORM
        data[AccountConstants.Analytics.CURRENT_SITE] = AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE
        analytics.sendGeneralEvent(data)
    }

    fun eventClickPaymentSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", AccountConstants.Analytics.SHOP,
                    AccountConstants.Analytics.SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
            )
        )
    }

    fun eventClickCreditCardSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val data = TrackAppUtils.gtmData(
            AccountConstants.Analytics.CLICK_PAYMENT,
            AccountConstants.Analytics.PAYMENT_SETTING_PAGE,
            String.format("%s %s", AccountConstants.Analytics.CLICK, item),
            ""
        )
        data[AccountConstants.Analytics.TRACKER_ID] = AccountConstants.Analytics.TRACKER_ID_42695
        data[AccountConstants.Analytics.BUSINESS_UNIT] = AccountConstants.Analytics.PAYMENT
        data[AccountConstants.Analytics.CURRENT_SITE] = AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE
        data[AccountConstants.Analytics.USER_ID] = userSessionInterface.userId
        analytics.sendGeneralEvent(data)
    }

    fun eventClickKycSetting(projectId: String) {
        track(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT, String.format("%s %s", AccountConstants.Analytics.ACCOUNT,
                    AccountConstants.Analytics.SETTING),
                AccountConstants.Analytics.CLICK_KYC_SETTING,
                "click - $projectId - ckyc"
            ), "2617"
        )
    }

    fun eventClickPinSetting() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT, String.format("%s %s", AccountConstants.Analytics.ACCOUNT,
                    AccountConstants.Analytics.SETTING),
                "click on button tokopedia pin",
                ""
            )
        )
    }

    fun eventClickSignInByPushNotifSetting() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_OTP, String.format("%s %s", AccountConstants.Analytics.ACCOUNT,
                    AccountConstants.Analytics.SETTING),
                "click masuk lewat notifikasi",
                ""
            )
        )
    }

    fun eventClickTokopediaCornerSetting() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                AccountConstants.Analytics.EVENT_CLICK_SAMPAI,
                AccountConstants.Analytics.EVENT_CATEGORY_SAMPAI,
                AccountConstants.Analytics.EVENT_ACTION_SAMPAI,
                ""
            )
        )
    }

    private fun track(data: MutableMap<String, Any>, trackerId: String) {
        data[AccountConstants.Analytics.BUSINESS_UNIT] = AccountConstants.Analytics.USER_PLATFORM
        data[AccountConstants.Analytics.CURRENT_SITE] = AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE
        data[AccountConstants.Analytics.TRACKER_ID] = trackerId
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }
}
