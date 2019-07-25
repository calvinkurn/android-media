package com.tokopedia.home.account.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.KYCConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

/**
 * Created by meta on 04/08/18.
 *
 * Setting PIN : https://docs.google.com/spreadsheets/d/1H3CSARG5QtVACiffBxd2HJE7adKzAZ3xxmtEbR01Eho/edit?ts=5ca30084#gid=1785281730
 */
public class AccountAnalytics {
    private final Context context;
    private UserSessionInterface userSessionInterface;

    public AccountAnalytics(Context context) {
        this.context = context;
        userSessionInterface = new UserSession(context);
    }

    public void eventClickAccount(String title, String section, String item, boolean withUserId) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        if (withUserId) {

            analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", AccountConstants.Analytics.CLICK, section, item),
                    userSessionInterface.getUserId()
            ));
        } else {
            analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                    CLICK_HOME_PAGE,
                    String.format("%s %s", AKUN_SAYA, title),
                    String.format("%s - %s - %s", AccountConstants.Analytics.CLICK, section, item),
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
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
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

    public void eventClickShopSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
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

    public void eventClickNotificationSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", NOTIFICATION, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
    }


    public void eventClickApplicationSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", APPLICATION, SETTING),
                String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ));
    }

    public void eventClickEmailSetting(String item) {

        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE,
                String.format("%s %s", EMAIL, SETTING),
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

    public void eventClickKYCSellerAccountPage(int status) {

        final Analytics analytics = TrackApp.getInstance().getGTM();
        switch (status) {
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
        eventTracking.put(EVENT_ACTION, String.format("%s %s", AccountConstants.Analytics.CLICK, NOTIFICATION));
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
        if (null != context && context.getApplicationContext() instanceof AccountHomeRouter)
            ((AccountHomeRouter) context.getApplicationContext()).sendAnalyticsUserAttribute(data);
    }

    public void setPromoPushPreference(Boolean newValue) {
        if (null != context && context.getApplicationContext() instanceof AccountHomeRouter)
            ((AccountHomeRouter) context.getApplicationContext()).setPromoPushPreference(newValue);
    }

    public void setNewsletterEmailPref(Boolean newValue) {
        if (null != context && context.getApplicationContext() instanceof AccountHomeRouter)
            ((AccountHomeRouter) context.getApplicationContext()).setNewsletterEmailPref(newValue);
    }

    public static void clickOpenShopFree(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_SELL,
                ACTION_CLICK_OPEN_SHOP,
                ""
        );
    }

    public static void clickKnowMore(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_SELL,
                ACTION_CLICK_LEARN_MORE,
                ""
        );
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

    public void eventAccountProductView(TrackingQueue trackingQueue, RecommendationItem recommendationItem, int position) {
        Map<String, Object> map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_ACCOUNT_PAGE_BUYER,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(
                                addAccountProductViewImpressions(recommendationItem, position)
                        )
                ));
        trackingQueue.putEETracking((HashMap<String, Object>) map);
    }

    private Object addAccountProductViewImpressions(RecommendationItem recommendationItem, int position) {
        String list =
                String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.getType());
        if (recommendationItem.isTopAds()) {
            list+=VALUE_PRODUCT_TOPADS;
        }
        return DataLayer.mapOf(DATA_NAME, recommendationItem.getName(),
                DATA_ID, recommendationItem.getProductId(),
                DATA_PRICE, recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                DATA_BRAND, NONE_OTHER,
                DATA_CATEGORY, recommendationItem.getCategoryBreadcrumbs(),
                DATA_VARIAN, NONE_OTHER,
                LIST, list,
                DATA_POSITION, String.valueOf(position));
    }

    public void eventAccountProductClick(RecommendationItem recommendationItem, int position, String widgetTitle) {
        String list =
                String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.getType());
        if (recommendationItem.isTopAds()) {
            list+=VALUE_PRODUCT_TOPADS;
        }
        final Analytics tracker = TrackApp.getInstance().getGTM();
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_ACCOUNT_PAGE_BUYER,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                    EVENT_LABEL, EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                            CLICK, DataLayer.mapOf(ACTION_FIELD,
                                    DataLayer.mapOf(LIST, list),
                                    PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                                            DATA_NAME, recommendationItem.getName(),
                                            DATA_ID, recommendationItem.getProductId(),
                                            DATA_PRICE, recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                                            DATA_BRAND, NONE_OTHER,
                                            DATA_CATEGORY, recommendationItem.getCategoryBreadcrumbs(),
                                            DATA_VARIAN, NONE_OTHER,
                                            LIST, widgetTitle,
                                            DATA_POSITION, String.valueOf(position),
                                            DATA_ATTRIBUTION, NONE_OTHER
                                    )))
                            )
            );
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }

    public void eventClickWishlistButton(boolean wishlistStatus) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        String status = "";
        if (wishlistStatus) {
            status = "add";
        } else {
            status = "remove";
        }

        if (analytics != null) {
            analytics.sendGeneralEvent(
                    CLICK_ACCOUNT,
                    EVENT_CATEGORY_ACCOUNT_PAGE_BUYER,
                    String.format(VALUE_WISHLIST_PRODUCT, status),
                    ""
            );
        }
    }
}
