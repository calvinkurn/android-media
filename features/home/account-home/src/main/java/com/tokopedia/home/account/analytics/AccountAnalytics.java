package com.tokopedia.home.account.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.KYCConstant;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.AKUN_SAYA;
import static com.tokopedia.home.account.AccountConstants.Analytics.APPLICATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_FINTECH_MICROSITE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_HOME_PAGE;
import static com.tokopedia.home.account.AccountConstants.Analytics.EMAIL;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_LABEL;
import static com.tokopedia.home.account.AccountConstants.Analytics.NOTIFICATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.SCREEN_NAME;
import static com.tokopedia.home.account.AccountConstants.Analytics.SCREEN_NAME_ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.SETTING;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOP_NAV;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER;

/**
 * Created by meta on 04/08/18.
 */
public class AccountAnalytics {

    private Context context;
    private UserSessionInterface userSessionInterface;

    public AccountAnalytics(Context context) {
        userSessionInterface = new UserSession(context);
    }

    public void eventClickAccount(String title, String section, String item, boolean withUserId) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        if (withUserId) {

            analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", CLICK, section, item),
                    userSessionInterface.getUserId()
            ));
        } else {
            analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", CLICK, section, item),
                    ""
            ));
        }
    }

    public void eventClickOVOPayLater(String category, String action, String label) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                CLICK_FINTECH_MICROSITE,
                category,
                action,
                label
        ));
    }

    public void eventClickSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }

    public void eventClickAccountSetting(String item) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", ACCOUNT, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }

    public void eventClickShopSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }

    public void eventClickPaymentSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }

    public void eventClickNotificationSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", NOTIFICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }


    public void eventClickApplicationSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", APPLICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ));
    }

    public void eventClickEmailSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", EMAIL, SETTING),
                String.format("%s %s", CLICK, item),
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

    public void eventClickKYCSellerAccountPage(int status) {

        final Analytics analytics = TrackApp.getInstance().getGTM();
        switch (status){
            case KYCConstant.STATUS_REJECTED:

                analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_REJECTED,
                        ""
                ));
                break;
            case KYCConstant.STATUS_EXPIRED:
                analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_REJECTED,
                        ""
                ));
                break;
            case KYCConstant.STATUS_PENDING:
                analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_PENDING,
                        ""
                ));
                break;
            case KYCConstant.STATUS_NOT_VERIFIED:
                analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_NOT_VERIFIED,
                        ""
                ));
                break;
            default:
                break;
        }
    }

    public void eventClickActivationOvoMyAccount() {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.EVENT_SALDO_OVO,
                AccountConstants.Analytics.MY_ACCOUNT,
                AccountConstants.Analytics.CLICK_MY_ACCOUNT_ACTIVATION_OVO,
                ""
        ));
    }

    public void eventTrackingNotification() {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_ACCOUNT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", CLICK, NOTIFICATION));
        eventTracking.put(EVENT_LABEL, "");

        analytics.sendGeneralEvent(eventTracking);
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

    public void setUserAttributes(UserAttributeData data) {
        ((AccountHomeRouter) context.getApplicationContext()).sendAnalyticsUserAttribute(data);
    }

    public void setPromoPushPreference(Boolean newValue) {
        ((AccountHomeRouter) context.getApplicationContext()).setPromoPushPreference(newValue);
    }

}
