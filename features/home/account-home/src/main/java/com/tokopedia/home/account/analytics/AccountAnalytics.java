package com.tokopedia.home.account.analytics;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.BUSINESS_UNIT;
import static com.tokopedia.home.account.AccountConstants.Analytics.CATEGORY_NOTIF_CENTER;
import static com.tokopedia.home.account.AccountConstants.Analytics.CATEGORY_SETTING_PAGE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_SETTING;
import static com.tokopedia.home.account.AccountConstants.Analytics.CURRENT_SITE;
import static com.tokopedia.home.account.AccountConstants.Analytics.EMPTY;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_CLICK_SCREEN_RECORDER;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_TS_USR_MENU;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_LABEL;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_NAME_CLICK_NOTIF_CENTER;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_SHOP_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_USER_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.FINGERPRINT;
import static com.tokopedia.home.account.AccountConstants.Analytics.HOME_AND_BROWSE;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.SETTING;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER_PLATFORM;

import android.content.Context;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meta on 04/08/18.
 * <p>
 * Setting PIN : https://docs.google.com/spreadsheets/d/1H3CSARG5QtVACiffBxd2HJE7adKzAZ3xxmtEbR01Eho/edit?ts=5ca30084#gid=1785281730
 */
public class AccountAnalytics {
    private final Context context;
    private UserSessionInterface userSessionInterface;

    public AccountAnalytics(Context context) {
        this.context = context;
        userSessionInterface = new UserSession(context);
    }

    public void eventTroubleshooterClicked() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> map = DataLayer.mapOf(
                EVENT, EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY, CATEGORY_NOTIF_CENTER,
                EVENT_ACTION, EVENT_ACTION_TS_USR_MENU,
                EVENT_LABEL, EMPTY,
                FIELD_USER_ID, userSessionInterface.getUserId(),
                FIELD_SHOP_ID, userSessionInterface.getShopId()

        );

        analytics.sendEnhanceEcommerceEvent(map);
    }

    public void homepageSaldoClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        TrackAnalytics.sendEvent(FirebaseEvent.Home.HAMBURGER_SALDO, map, context);
    }

    public void eventClickSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
    }

    public void eventClickAdvancedSetting(String item) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_SETTING,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));

    }

    public void eventClickScreenRecorder() {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(EVENT, CLICK_SETTING);
        eventTracking.put(EVENT_CATEGORY, CATEGORY_SETTING_PAGE);
        eventTracking.put(EVENT_ACTION, EVENT_ACTION_CLICK_SCREEN_RECORDER);
        eventTracking.put(EVENT_LABEL, "");
        eventTracking.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        eventTracking.put(BUSINESS_UNIT, HOME_AND_BROWSE);

        analytics.sendGeneralEvent(eventTracking);
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

    public void eventClickKycSetting() {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s", ACCOUNT, SETTING),
                AccountConstants.Analytics.CLICK_KYC_SETTING,
                ""
        ));
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

    public void eventClickToggleOnGeolocation(Context context) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        if (analytics != null) {
            analytics.sendGeneralEvent(
                    "clickHomePage",
                    "homepage",
                    "click toggle on geolocation",
                    ""
            );
        }
    }

    public void eventClickToggleOffGeolocation(Context context) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        if (analytics != null) {
            analytics.sendGeneralEvent(
                    "clickHomePage",
                    "homepage",
                    "click toggle off geolocation",
                    ""
            );
        }
    }

    public void eventClickThemeSetting(boolean isDarkMode) {
        String label;
        if(isDarkMode) {
            label = "dark";
        } else {
            label = "light";
        }
        final Analytics analytics = TrackApp.getInstance().getGTM();
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_SETTING,
                AccountConstants.Analytics.CATEGORY_SETTING_PAGE,
                AccountConstants.Analytics.ACTION_SIMPAN_THEME_SELECTION,
                label
        ));
    }
}
