package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * this class contains tracking for Unify (Click, Discovery & View Product, Authentication,
 * Login/Register)
 */

public class UnifyTracking extends TrackingUtils {
    public static final String EXTRA_LABEL = "label";

 public static void eventPushNotifLowTopadsReceived(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_LOW_CREDIT
        ).getEvent());
    }

    public static void eventPushNotifSuccessTopadsReceived(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_SUCCESS_TOPUP
        ).getEvent());
    }

    public static void eventTopAdsProductEditGrupCost(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_COST + budgetOption);
    }

    public static void eventTopAdsShopAddPromoShowTime(Context context, String showTimeOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.ADD_SHOP_PROMO_SHOWTIME + showTimeOption);
    }

    public static void eventTopAdsProductEditGrupSchedule(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_SCHEDULE + showTimeOption);
    }

    public static void eventTopAdsProductEditProductSchedule(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_PRODUCT_SCHEDULE + showTimeOption);
    }

    public static void eventTopAdsProductEditGroupName(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_NAME);
    }

    public static void eventTopAdsProductAddPromoStep2(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_2 + budgetOption);
    }

    public static void eventTopAdsProductDeleteGrup(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DELETE_GROUP);
    }

    public static void eventTopAdsProductDetailGroupPageDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_GROUP_DETAIL);
    }

    public static void eventTopAdsProductDetailGroupPageDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_GROUP_DETAIL + periodOption);
    }

    public static void eventTopAdsProductClickDetailGroupPDP(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DETAIL_PROMO_PRODUCT_PDP);
    }

    public static void eventTopAdsProductClickDetailProductPDP(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DETAIL_PROMO_PRODUCT_GROUP);
    }

    public static void eventTopAdsProductDetailProductPageDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_PRODUCT_DETAIL);
    }

    public static void eventTopAdsProductDetailProductPageDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_PRODUCT_DETAIL + periodOption);
    }

    public static void eventTopAdsProductEditGrupManage(Context context, String groupOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_MANAGE_GROUP + groupOption);
    }

    public static void eventTopAdsShop(Context context, String eventLabel) {
        eventTopAds(context, AppEventTracking.Category.TOP_ADS_SHOP, eventLabel);
    }

    public static void eventTopAdsShopAddPromoBudget(Context context, String budgetOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.ADD_SHOP_PROMO_BUDGET + budgetOption);
    }

    public static void eventTopAdsProductNewPromo(Context context, String promoOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO + promoOption);
    }

    public static void eventTopadsEditGroupPromoAddProduct(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_ADD_PRODUCT);
    }

    public static void eventTopAdsProductGroupsFilter(Context context, String groupFilterOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.GROUPS_FILTER + groupFilterOption);
    }

    public static void eventTopAdsProductNewPromoGroup(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_GROUP);
    }

    public static void eventTopAdsProductPageGroupDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_GROUP);
    }

    public static void eventTopAdsProductPageGroupDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_GROUP + periodOption);
    }

    public static void eventTopAdsProductNewPromoProduct(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_PRODUCT);
    }

    public static void eventTopAdsProductPageProductDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_PRODUCT + periodOption);
    }

    public static void eventTopAdsProductEditProductCost(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_PRODUCT_COST + budgetOption);
    }

    public static void eventTopAdsProductAddPromoWithoutGroupStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_WITHOUT_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoExistingGroupStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_EXISTING_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoWithoutGroupStep2(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_WITHOUT_GROUP_STEP_2 + budgetOption);
    }

    public static void eventTopAdsProductAddPromoStep3(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_3 + showTimeOption);
    }

    public static void eventTopAdsProductPageProductDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_PRODUCT);
    }

    public static void eventTopAds(Context context, String category, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TOP_ADS_SELLER_APP,
                category,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventTopAdsProduct(Context context, String eventLabel) {
        eventTopAds(context, AppEventTracking.Category.TOP_ADS_PRODUCT, eventLabel);
    }

    public static void eventTopAdsProductNewKeyword(Context context, String keywordTypeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.KEYWORD_POSITIF + keywordTypeOption);
    }

    public static void eventTopAdsProductNewKeywordNegatif(Context context, String keywordTypeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.KEYWORD_NEGATIF + keywordTypeOption);
    }

    public static void eventClickGMStatProduct(Context context, String eventLabel) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_PRODUCT, eventLabel);
    }

    public static void eventClickGMStatBuyGMDetailTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STAT,
                AppEventTracking.EventLabel.BUY_GM);
    }

    public static void eventClickGMStatDatePickerWOCompareTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_DATE_PICKER,
                AppEventTracking.EventLabel.CHANGE_DATE);
    }

    public static void eventClickGMStatDatePickerWCompareTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_DATE_PICKER,
                AppEventTracking.EventLabel.CHANGE_DATE_COMPARE);
    }

    public static void eventClickGMStatManageTopAds(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_TOP_ADS,
                AppEventTracking.EventLabel.MANAGE_TOPADS);
    }

    public static void eventClickGMStatProductSoldTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_TRANSACTION_DETAIL,
                AppEventTracking.EventLabel.PRODUCT_SOLD);
    }

    public static void eventClickGMStatFilterNameTransaction(Context context, String filterName) {
        eventClickFilterGMStat(context, AppEventTracking.Category.GM_STATISTIC_TRANSACTION_DETAIL,
                AppEventTracking.EventLabel.GRAPH_X + filterName);
    }

    public static void eventClickGMStatSeeDetailTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GMSTAT_TRANSACTION,
                AppEventTracking.EventLabel.SEE_DETAIL);
    }

    public static void eventClickGMStatFilterTypeProductSold(Context context, String filterType) {
        eventClickFilterGMStat(context, AppEventTracking.Category.GM_STATISTIC_PRODUCT_SOLD, filterType);
    }

    private static void eventClickFilterGMStat(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.FILTER,
                eventLabel
        ).getEvent());
    }

    private static void eventClickGMStat(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventLoadGMStat(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LOAD_GM_STAT,
                AppEventTracking.Category.FULLY_LOAD,
                AppEventTracking.Action.LOAD,
                AppEventTracking.EventLabel.STATISTIC_PAGE
        ).getEvent());
    }

    public static void eventScrollGMStat(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SCROLL_GM_STAT,
                AppEventTracking.Category.GM_STAT,
                AppEventTracking.Action.SCROLL,
                AppEventTracking.EventLabel.ONE_HUNDRED_PERCENT
        ).getEvent());
    }

    public static void eventCampaign(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                label
        ).getEvent());
    }

    public static void eventShare(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE_TO + label
        ).getEvent());
    }

    public static void eventReferralAndShare(Context context, String action, String label) {
        Map<String,Object> gtmMap=new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_REFERRAL,
                AppEventTracking.Category.REFERRAL,
                action,
                "Android"
        ).getEvent();
        gtmMap.put("channel",label);
        gtmMap.put("source","referral");
        TrackApp.getInstance().getGTM().sendGeneralEvent(gtmMap);
    }

    public static void eventAppShareWhenReferralOff(Context context, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF,
                AppEventTracking.Category.APPSHARE,
                action,
                label
        ).getEvent());
    }

    /**
     * use trackApp instead
     */
    @Deprecated
    public static void eventTracking(Context context, EventTracking eventTracking) {
        sendGTMEvent(context, eventTracking.getEvent());
    }

    public static void eventDigitalEventTracking(Context context, String action, String label) {
        UserSessionInterface userSession = new UserSession(context);
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_DIGITAL_EVENT,
                AppEventTracking.Category.DIGITAL_EVENT,
                action,
                label
        ).setUserId(userSession.getUserId()).getEvent());
    }

}
