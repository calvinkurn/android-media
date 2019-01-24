package com.tokopedia.home.account.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.home.account.AccountConstants;
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

    private AnalyticTracker analyticTracker;
    private Context context;
    private UserSessionInterface userSessionInterface;

    public AccountAnalytics(Context context) {
        if (context == null)
            return;

        this.context = context;

        if (context.getApplicationContext() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) context.getApplicationContext())
                    .getAnalyticTracker();
        }

        userSessionInterface = new UserSession(context);
    }

    public void eventClickAccount(String title, String section, String item, boolean withUserId) {
        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        if (withUserId) {

            gtmAnalytics.pushGeneral(new EventTracking(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", CLICK, section, item),
                    userSessionInterface.getUserId()
            ).getEvent());
        } else {
            gtmAnalytics.pushGeneral(new EventTracking(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", CLICK, section, item),
                    ""
            ).getEvent());
        }
    }

    public void eventClickOVOPayLater(String category, String action, String label) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                CLICK_FINTECH_MICROSITE,
                category,
                action,
                label
        ).getEvent());
    }

    public void eventClickSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickAccountSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", ACCOUNT, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickShopSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickPaymentSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickNotificationSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", NOTIFICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }


    public void eventClickApplicationSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", APPLICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickEmailSetting(String item) {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", EMAIL, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        ).getEvent());
    }

    public void eventClickKycSetting() {
        if (analyticTracker == null)
            return;

        GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s", ACCOUNT, SETTING),
                AccountConstants.Analytics.CLICK_KYC_SETTING,
                ""
        ).getEvent());
    }

    public void eventClickKYCSellerAccountPage(int status) {
        if (analyticTracker == null) {
            return;
        }

        final GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");
        switch (status){
            case KYCConstant.STATUS_REJECTED:

                gtmAnalytics.pushGeneral(new EventTracking(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_REJECTED,
                        ""
                ).getEvent());
                break;
            case KYCConstant.STATUS_EXPIRED:
                gtmAnalytics.pushGeneral(new EventTracking(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_REJECTED,
                        ""
                ).getEvent());
                break;
            case KYCConstant.STATUS_PENDING:
                gtmAnalytics.pushGeneral(new EventTracking(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_PENDING,
                        ""
                ).getEvent());
                break;
            case KYCConstant.STATUS_NOT_VERIFIED:
                gtmAnalytics.pushGeneral(new EventTracking(
                        AccountConstants.Analytics.CLICK_ACCOUNT,
                        String.format("%s %s", ACCOUNT, SETTING),
                        AccountConstants.Analytics.CLICK_KYC_NOT_VERIFIED,
                        ""
                ).getEvent());
                break;
            default:
                break;
        }
    }

    public void eventClickActivationOvoMyAccount() {
        final GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.EVENT_SALDO_OVO,
                AccountConstants.Analytics.MY_ACCOUNT,
                AccountConstants.Analytics.CLICK_MY_ACCOUNT_ACTIVATION_OVO,
                ""
        ).getEvent());
    }

    public void eventTrackingNotification() {
        if (analyticTracker == null)
            return;

        final GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_ACCOUNT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", CLICK, NOTIFICATION));
        eventTracking.put(EVENT_LABEL, "");

        gtmAnalytics.pushGeneral(eventTracking);
    }

    public void eventClickTokopediaCornerSetting() {
        if (analyticTracker == null)
            return;

        final GTMAnalytics gtmAnalytics = (GTMAnalytics) TrackApp.getInstance().getValue("GTM");

        gtmAnalytics.pushGeneral(new EventTracking(
                AccountConstants.Analytics.EVENT_CLICK_SAMPAI,
                AccountConstants.Analytics.EVENT_CATEGORY_SAMPAI,
                AccountConstants.Analytics.EVENT_ACTION_SAMPAI,
                ""
        ).getEvent());
    }

    public void setUserAttributes(UserAttributeData data) {
        ((AccountHomeRouter) context.getApplicationContext()).sendAnalyticsUserAttribute(data);
    }

    public void setPromoPushPreference(Boolean newValue) {
        ((AccountHomeRouter) context.getApplicationContext()).setPromoPushPreference(newValue);
    }
}
