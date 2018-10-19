package com.tokopedia.home.account.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.AccountHomeRouter;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

/**
 * Created by meta on 04/08/18.
 */
public class AccountAnalytics {

    private AnalyticTracker analyticTracker;
    private Context context;

    public AccountAnalytics(Context context) {
        if (context == null)
            return;

        this.context = context;

        if (context.getApplicationContext() instanceof AbstractionRouter) {
            analyticTracker = ((AbstractionRouter) context.getApplicationContext())
                    .getAnalyticTracker();
        }
    }

    public void eventClickAccount(String title, String section, String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                CLICK_HOME_PAGE,
                String.format("%s %s", AKUN_SAYA, title),
                String.format("%s - %s - %s", CLICK, section, item),
                ""
        );
    }

    public void eventClickSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickAccountSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", ACCOUNT, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickShopSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickPaymentSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickNotificationSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", NOTIFICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }


    public void eventClickApplicationSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", APPLICATION, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickEmailSetting(String item) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", EMAIL, SETTING),
                String.format("%s %s", CLICK, item),
                ""
        );
    }

    public void eventClickActivationOvoMyAccount() {
        analyticTracker.sendEventTracking(
                AccountConstants.Analytics.EVENT_SALDO_OVO,
                AccountConstants.Analytics.MY_ACCOUNT,
                AccountConstants.Analytics.CLICK_MY_ACCOUNT_ACTIVATION_OVO, "");
    }

    public void eventTrackingNotification() {
        if (analyticTracker == null)
            return;

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_ACCOUNT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", CLICK, NOTIFICATION));
        eventTracking.put(EVENT_LABEL, "");

        analyticTracker.sendEventTracking(eventTracking);
    }

    public void setUserAttributes(UserAttributeData data) {
        ((AccountHomeRouter) context.getApplicationContext()).sendAnalyticsUserAttribute(data);
    }

    public void setPromoPushPreference(Boolean newValue) {
        ((AccountHomeRouter) context.getApplicationContext()).setPromoPushPreference(newValue);
    }
}
