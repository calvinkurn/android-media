package com.tokopedia.home_account.account_settings.analytics;

import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.ACCOUNT;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.BUSINESS_UNIT;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.CURRENT_SITE;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.FINGERPRINT;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.SETTING;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.SHOP;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.TRACKER_ID;
import static com.tokopedia.home_account.account_settings.AccountConstants.Analytics.USER_PLATFORM;

import android.content.Context;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.home_account.account_settings.AccountConstants;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by meta on 04/08/18.
 * <p>
 * Setting PIN : https://docs.google.com/spreadsheets/d/1H3CSARG5QtVACiffBxd2HJE7adKzAZ3xxmtEbR01Eho/edit?ts=5ca30084#gid=1785281730
 */
public class AccountAnalytics {
    private final Context context;
    private UserSessionInterface userSessionInterface;

    @Inject
    public AccountAnalytics(Context context, UserSessionInterface userSessionInterface) {
        this.context = context;
        this.userSessionInterface = userSessionInterface;
    }

    public AccountAnalytics(Context context) {
        this.context = context;
        userSessionInterface = new UserSession(context);
    }

    public void homepageSaldoClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        TrackAnalytics.sendEvent(FirebaseEvent.Home.HAMBURGER_SALDO, map, context);
    }

    public void eventClickAccountSetting(String item) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", ACCOUNT, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
    }

    public void eventClickAccountPassword() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s - %s", ACCOUNT, SETTING, PASSWORD),
                AccountConstants.Analytics.CLICK_ON_PASSWORD,
                ""
        ));
    }

    /* Tracker no.6 */
    public void eventClickFingerprint() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> data = TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT_SETTING,
                String.format("%s %s", ACCOUNT, SETTING),
                AccountConstants.Analytics.CLICK_BIOMETRIC_BUTTON,
                String.format("%s - %s", CLICK, FINGERPRINT)
        );

        data.put(BUSINESS_UNIT, USER_PLATFORM);
        data.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        analytics.sendGeneralEvent(data);
    }

    public void eventClickPaymentSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
    }

    public void eventClickKycSetting(String projectId) {
        track(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s", ACCOUNT, SETTING),
                AccountConstants.Analytics.CLICK_KYC_SETTING,
                "click - " + projectId + " - ckyc"
        ), "2617");
    }

    public void eventClickPinSetting() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s", ACCOUNT, SETTING),
                "click on button tokopedia pin",
                ""
        ));
    }

    public void eventClickSignInByPushNotifSetting() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_OTP,
                String.format("%s %s", ACCOUNT, SETTING),
                "click masuk lewat notifikasi",
                ""
        ));
    }

    public void eventClickTokopediaCornerSetting() {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.EVENT_CLICK_SAMPAI,
                AccountConstants.Analytics.EVENT_CATEGORY_SAMPAI,
                AccountConstants.Analytics.EVENT_ACTION_SAMPAI,
                ""
        ));
    }

    private void track(Map<String, Object> data, String trackerId) {
        data.put(BUSINESS_UNIT, USER_PLATFORM);
        data.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        data.put(TRACKER_ID, trackerId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(data);
    }
}
