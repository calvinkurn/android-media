package com.tokopedia.home.account.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.KYCConstant;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.home.account.AccountConstants.Analytics.ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.ACTION_CLICK_LEARN_MORE;
import static com.tokopedia.home.account.AccountConstants.Analytics.ACTION_CLICK_OPEN_SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.ACTION_FIELD;
import static com.tokopedia.home.account.AccountConstants.Analytics.AKUN_SAYA;
import static com.tokopedia.home.account.AccountConstants.Analytics.BUSINESS_UNIT;
import static com.tokopedia.home.account.AccountConstants.Analytics.CATEGORY_ACCOUNT_SELL;
import static com.tokopedia.home.account.AccountConstants.Analytics.CATEGORY_NOTIF_CENTER;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_FINTECH_MICROSITE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_HOME_PAGE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CURRENCY_CODE;
import static com.tokopedia.home.account.AccountConstants.Analytics.CURRENT_SITE;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_ATTRIBUTION;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_BRAND;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_CATEGORY;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_DIMENSION_83;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_NAME;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_POSITION;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_PRICE;
import static com.tokopedia.home.account.AccountConstants.Analytics.DATA_VARIAN;
import static com.tokopedia.home.account.AccountConstants.Analytics.ECOMMERCE;
import static com.tokopedia.home.account.AccountConstants.Analytics.EMPTY;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_ACCOUNT_PROMOTION_IMPRESSION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_DALAM_PROSES;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_ETICKET_EVOUCHER;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_MENUNGGU_PEMBAYARAN;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_SEMUA_TRANSAKSI;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_ACTION_TS_USR_MENU;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY_ACCOUNT_PAGE_BUYER;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CATEGORY_AKUN_PEMBELI;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_CLICK_ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_LABEL;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_NAME_CLICK_NOTIF_CENTER;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_PRODUCT_CLICK;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_PRODUCT_VIEW;
import static com.tokopedia.home.account.AccountConstants.Analytics.EVENT_PROMO_VIEW;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_CREATIVE;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_CREATIVE_URL;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_NAME;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_POSITION;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_SHOP_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.FIELD_USER_ID;
import static com.tokopedia.home.account.AccountConstants.Analytics.IDR;
import static com.tokopedia.home.account.AccountConstants.Analytics.IMPRESSIONS;
import static com.tokopedia.home.account.AccountConstants.Analytics.INBOX;
import static com.tokopedia.home.account.AccountConstants.Analytics.ITEM_POWER_MERCHANT;
import static com.tokopedia.home.account.AccountConstants.Analytics.LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.NONE_OTHER;
import static com.tokopedia.home.account.AccountConstants.Analytics.NOTIFICATION;
import static com.tokopedia.home.account.AccountConstants.Analytics.PASSWORD;
import static com.tokopedia.home.account.AccountConstants.Analytics.PENJUAL;
import static com.tokopedia.home.account.AccountConstants.Analytics.PRODUCTS;
import static com.tokopedia.home.account.AccountConstants.Analytics.PROMOTIONS;
import static com.tokopedia.home.account.AccountConstants.Analytics.PROMOTION_VIEW;
import static com.tokopedia.home.account.AccountConstants.Analytics.SCREEN_NAME;
import static com.tokopedia.home.account.AccountConstants.Analytics.SCREEN_NAME_ACCOUNT;
import static com.tokopedia.home.account.AccountConstants.Analytics.SECTION_OTHER_FEATURE;
import static com.tokopedia.home.account.AccountConstants.Analytics.SETTING;
import static com.tokopedia.home.account.AccountConstants.Analytics.SHOP;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOKOPEDIA_MARKETPLACE;
import static com.tokopedia.home.account.AccountConstants.Analytics.TOP_NAV;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER_BELI;
import static com.tokopedia.home.account.AccountConstants.Analytics.USER_PLATFORM;
import static com.tokopedia.home.account.AccountConstants.Analytics.VALUE_ACCOUNT_PROMOTION_NAME;
import static com.tokopedia.home.account.AccountConstants.Analytics.VALUE_BEBAS_ONGKIR;
import static com.tokopedia.home.account.AccountConstants.Analytics.VALUE_PRODUCT_RECOMMENDATION_LIST;
import static com.tokopedia.home.account.AccountConstants.Analytics.VALUE_PRODUCT_TOPADS;
import static com.tokopedia.home.account.AccountConstants.Analytics.VALUE_WISHLIST_PRODUCT;

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

    public void eventTokopediaPayClick(String event, String category, String action, String label) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                event,
                String.format("%s %s", AKUN_SAYA, category),
                String.format("%s - %s - %s", AccountConstants.Analytics.CLICK, action, label),
                label
        ));
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

    public void homepageSaldoClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        TrackAnalytics.sendEvent(FirebaseEvent.Home.HAMBURGER_SALDO, map, context);
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

    public void eventClickAdvancedSetting(String item) {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_SETTING,
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

    public void eventClickAccountPassword() {
        Analytics analytics = TrackApp.getInstance().getGTM();

        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_ACCOUNT,
                String.format("%s %s - %s", ACCOUNT, SETTING, PASSWORD),
                AccountConstants.Analytics.CLICK_ON_PASSWORD,
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

    public void eventManageShopShipping(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.SHOP_MANAGE,
                AccountConstants.Analytics.CATEGORY_SHOP_MANAGE,
                AccountConstants.Analytics.SHOP_CLICK,
                AccountConstants.Analytics.SHOP_SHIPPING
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

    public void eventTrackingInbox() {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, SCREEN_NAME_ACCOUNT);
        eventTracking.put(EVENT, CLICK_HOME_PAGE);
        eventTracking.put(EVENT_CATEGORY, TOP_NAV);
        eventTracking.put(EVENT_ACTION, String.format("%s %s", AccountConstants.Analytics.CLICK, INBOX));
        eventTracking.put(EVENT_LABEL, "");

        analytics.sendGeneralEvent(eventTracking);
    }

     public void clickMenungguPembayaranUoh(String userId) {
         final Analytics analytics = TrackApp.getInstance().getGTM();

         Map<String, Object> eventTracking = new HashMap<>();
         eventTracking.put(SCREEN_NAME, USER_BELI);
         eventTracking.put(EVENT, EVENT_CLICK_ACCOUNT);
         eventTracking.put(EVENT_CATEGORY, EVENT_CATEGORY_AKUN_PEMBELI);
         eventTracking.put(EVENT_ACTION, EVENT_ACTION_MENUNGGU_PEMBAYARAN);
         eventTracking.put(EVENT_LABEL, "");
         eventTracking.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
         eventTracking.put(FIELD_USER_ID, userId);
         eventTracking.put(BUSINESS_UNIT, USER_PLATFORM);

         analytics.sendGeneralEvent(eventTracking);
     }

    public void clickSemuaTransaksiUoh(String userId) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, USER_BELI);
        eventTracking.put(EVENT, EVENT_CLICK_ACCOUNT);
        eventTracking.put(EVENT_CATEGORY, EVENT_CATEGORY_AKUN_PEMBELI);
        eventTracking.put(EVENT_ACTION, EVENT_ACTION_SEMUA_TRANSAKSI);
        eventTracking.put(EVENT_LABEL, "");
        eventTracking.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        eventTracking.put(FIELD_USER_ID, userId);
        eventTracking.put(BUSINESS_UNIT, USER_PLATFORM);

        analytics.sendGeneralEvent(eventTracking);
    }

    public void clickDalamProsesUoh(String userId) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, USER_BELI);
        eventTracking.put(EVENT, EVENT_CLICK_ACCOUNT);
        eventTracking.put(EVENT_CATEGORY, EVENT_CATEGORY_AKUN_PEMBELI);
        eventTracking.put(EVENT_ACTION, EVENT_ACTION_DALAM_PROSES);
        eventTracking.put(EVENT_LABEL, "");
        eventTracking.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        eventTracking.put(FIELD_USER_ID, userId);
        eventTracking.put(BUSINESS_UNIT, USER_PLATFORM);

        analytics.sendGeneralEvent(eventTracking);
    }

    public void clickEticketEvoucherUoh(String userId) {
        final Analytics analytics = TrackApp.getInstance().getGTM();

        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SCREEN_NAME, USER_BELI);
        eventTracking.put(EVENT, EVENT_CLICK_ACCOUNT);
        eventTracking.put(EVENT_CATEGORY, EVENT_CATEGORY_AKUN_PEMBELI);
        eventTracking.put(EVENT_ACTION, EVENT_ACTION_ETICKET_EVOUCHER);
        eventTracking.put(EVENT_LABEL, "");
        eventTracking.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        eventTracking.put(FIELD_USER_ID, userId);
        eventTracking.put(BUSINESS_UNIT, USER_PLATFORM);

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

    public void setUserAttributes(UserAttributeData profileData) {
        if(context != null){
            MoengageInteractor.INSTANCE.setUserDataGraphQL(profileData.getProfile() == null ? "" : profileData.getProfile().getFullName(),
                    profileData.getProfile() != null && profileData.getProfile().getFirstName() != null ? profileData.getProfile().getFirstName() : "",
                    profileData.getProfile() == null ? "" : profileData.getProfile().getUserId(),
                    profileData.getProfile() == null ? "" : profileData.getProfile().getEmail(),
                    profileData.getProfile() == null || profileData.getProfile().getPhone() == null ? "" : profileData.getProfile().getPhone(),
                    profileData.getProfile().getBday() != null ? profileData.getProfile().getBday() : "",
                    profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getInfo().getShopName() : "",
                    profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getInfo().getShopId() : "",
                    profileData.getUserShopInfo() != null ? profileData.getUserShopInfo().getStats().getShopItemSold() : "0",
                    profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getTopadsAmount() + "" : "",
                    profileData.getPaymentAdminProfile() != null ? profileData.getPaymentAdminProfile().getLastPurchaseDate() : "",
                    profileData.getProfile().getGender() != null ? profileData.getProfile().getGender() : "0",
                    profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() != null ? profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() : false);
            //update on the basis of moengage flag
            if (!TextUtils.isEmpty(userSessionInterface.getDeviceId()))
                MoengageInteractor.INSTANCE.refreshToken(userSessionInterface.getDeviceId());
        }
    }

    public static void clickOpenShopFree() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_SELL,
                ACTION_CLICK_OPEN_SHOP,
                ""
        );
    }

    public static void clickKnowMore() {
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
                String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.getRecommendationType());
        if (recommendationItem.isTopAds()) {
            list += VALUE_PRODUCT_TOPADS;
        }
        return DataLayer.mapOf(DATA_NAME, recommendationItem.getName(),
                DATA_ID, recommendationItem.getProductId(),
                DATA_PRICE, recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                DATA_BRAND, NONE_OTHER,
                DATA_CATEGORY, recommendationItem.getCategoryBreadcrumbs(),
                DATA_VARIAN, NONE_OTHER,
                LIST, list,
                DATA_POSITION, String.valueOf(position),
                DATA_DIMENSION_83, recommendationItem.isFreeOngkirActive() ? VALUE_BEBAS_ONGKIR : NONE_OTHER);
    }

    public void eventAccountProductClick(RecommendationItem recommendationItem, int position, String widgetTitle) {
        String list =
                String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.getRecommendationType());
        if (recommendationItem.isTopAds()) {
            list += VALUE_PRODUCT_TOPADS;
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
                                            DATA_ATTRIBUTION, NONE_OTHER,
                                            DATA_DIMENSION_83, recommendationItem.isFreeOngkirActive() ? VALUE_BEBAS_ONGKIR : NONE_OTHER
                                    )))
                    )
            );
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }

    public void eventAccountPromoClick(String label) {
        final Analytics analytics = TrackApp.getInstance().getGTM();
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                EVENT_CATEGORY_AKUN_PEMBELI,
                AccountConstants.Analytics.EVENT_ACTION_ACCOUNT_PROMOTION_CLICK, label
        ));
    }

    public void eventAccountPromoRewardClick() {
        final Analytics analytics = TrackApp.getInstance().getGTM();
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                EVENT_CATEGORY_AKUN_PEMBELI,
                AccountConstants.Analytics.EVENT_ACTION_ACCOUNT_PROMOTION_REWARD_CLICK, ""
        ));
    }

    public static HashMap<String, Object> getAccountPromoImpression(String creativeName, int position) {
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_PROMO_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_AKUN_PEMBELI,
                EVENT_ACTION, EVENT_ACTION_ACCOUNT_PROMOTION_IMPRESSION,
                EVENT_LABEL, EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMOTION_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                                        FIELD_ID, 0,
                                        FIELD_NAME, VALUE_ACCOUNT_PROMOTION_NAME,
                                        FIELD_CREATIVE, creativeName,
                                        FIELD_CREATIVE_URL, NONE_OTHER,
                                        FIELD_POSITION, String.valueOf(position)
                                )))
                )
        );
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

    public void eventTrackingNotifCenter() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(dataNotifCenter());
    }

    public void eventClickPowerMerchantSetting() {
        final Analytics analytics = TrackApp.getInstance().getGTM();
        final String category = String.format("%s %s", AKUN_SAYA, PENJUAL);
        final String action = String.format("%s - %s - %s", CLICK, SECTION_OTHER_FEATURE, ITEM_POWER_MERCHANT);

        final Map<String, Object> event = TrackAppUtils.gtmData(
            CLICK_HOME_PAGE,
            category,
            action,
            ""
        );

        event.put(FIELD_USER_ID, userSessionInterface.getUserId());
        event.put(FIELD_SHOP_ID, userSessionInterface.getShopId());
        event.put(AccountConstants.Analytics.FIELD_SHOP_TYPE, getShopType());

        analytics.sendGeneralEvent(event);
    }

    private String getShopType() {
        if(userSessionInterface.isGoldMerchant()) {
            return AccountConstants.Analytics.SHOP_TYPE_PM;
        } else {
            return AccountConstants.Analytics.SHOP_TYPE_RM;
        }
    }

    private Map<String, Object> dataNotifCenter() {
        Map<String, Object> trackerMap = new HashMap<>();
        trackerMap.put(EVENT, AccountConstants.Analytics.CLICK_NOTIF_CENTER);
        trackerMap.put(EVENT_CATEGORY, AccountConstants.Analytics.NOTIF_CENTER);
        trackerMap.put(EVENT_ACTION, AccountConstants.Analytics.NOTIF_CENTER_ACTION);
        trackerMap.put(EVENT_LABEL, "");
        return trackerMap;
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
