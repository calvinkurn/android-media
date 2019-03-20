package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.firebase.perf.metrics.Trace;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tkpd.library.utils.legacy.CurrencyFormatHelper;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.track.TrackApp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

@Deprecated
public class TrackingUtils extends TrackingConfig {
    public static void eventCampaign(Context context, Campaign campaign) {
        Campaign temp = new Campaign(campaign);
        getGTMEngine(context)
                .sendCampaign(temp)
                .clearCampaign(campaign);
    }

    public static void activityBasedAFEvent(Context context, String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_HOME_ACTIVITY)) {
            afValue.put(AFInAppEventParameterName.PARAM_1, CommonUtils.getUniqueDeviceID(context));
        }
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AppScreen.convertAFActivityEvent(tag), afValue);
    }

    public static String getNetworkSpeed(Context context) {
        if (ConnectivityUtils.isConnected(context)) {
            return ConnectivityUtils.getConnectionType(context);
        } else {
            return ConnectivityUtils.CONN_UNKNOWN;
        }
    }

    public static String extractFirstSegment(Context context,String inputString, String separator) {
        String firstSegment = "";
        if (!TextUtils.isEmpty(inputString)) {
            String token[] = inputString.split(separator);
            if (token.length > 1) {
                firstSegment = token[0];
            } else {
                firstSegment = separator;
            }
        }

        return firstSegment;
    }

    private static String getFirstName(String name) {
        String firstName = name;
        if (!TextUtils.isEmpty(name)) {
            String token[] = name.split(" ");
            if (token.length > 1) {
                firstName = token[0];
            }
        }

        return firstName;
    }

    public static String normalizePhoneNumber(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum))
            return phoneNum.replaceFirst("^0(?!$)", "62");
        else
            return "";
    }

    public static void sendMoEngageClickMainCategoryIcon(Context context, String categoryName) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CATEGORY, categoryName
        );
        TrackApp.getInstance().getMoEngage().sendEvent(value, AppEventTracking.EventMoEngage.CLICK_MAIN_CATEGORY_ICON);
    }

    public static void sendMoEngageFavoriteEvent(Context context, String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.SHOP_NAME, shopName,
                AppEventTracking.MOENGAGE.SHOP_ID, shopID,
                AppEventTracking.MOENGAGE.SHOP_LOCATION, shopLocation,
                AppEventTracking.MOENGAGE.SHOP_URL_SLUG, shopDomain,
                AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, isShopOfficaial
        );
        TrackApp.getInstance().getMoEngage().sendEvent(value,
                isFollowed ?
                        AppEventTracking.EventMoEngage.SELLER_ADDED_FAVORITE :
                        AppEventTracking.EventMoEngage.SELLER_REMOVE_FAVORITE);
    }

    public static void sendMoEngageReferralShareEvent(Context context,String channel) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CHANNEL, channel
        );
        TrackApp.getInstance().getMoEngage().sendEvent(value, AppEventTracking.EventMoEngage.REFERRAL_SHARE_EVENT);
    }

    public static void fragmentBasedAFEvent(Context context,String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT)
                || tag.equals(AppScreen.IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "register_normal");
        } else if (tag.equals(AppScreen.IDENTIFIER_CATEGORY_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_HOME_MAIN);
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AppScreen.convertAFFragmentEvent(tag), afValue);
    }

    public static void eventError(Context context,String className, String errorMessage) {
        getGTMEngine(context)
                .eventError(className, errorMessage);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventOnline(Context context,String uid) {
        getGTMEngine(context)
                .eventOnline(uid);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventPushUserID(Context context,String userId) {
        getGTMEngine(context)
                .pushUserId(userId);
    }

    public static void eventAppsFlyerViewListingSearch(Context context,JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.SEARCH, listViewEvent);
    }

    public static void sendGTMEvent(Context context, Map<String, Object> dataLayers) {
        getGTMEngine(context).sendEvent(dataLayers);
    }

    public static void sendAppsFlyerDeeplink(Activity activity) {
        TrackApp.getInstance().getAppsFlyer().sendDeeplinkData(activity);
    }

    public static String getClientID(Context context) {
        return getGTMEngine(context).getClientIDString();
    }

    public static String getAfUniqueId(Context context) {
        return TrackApp.getInstance().getAppsFlyer().getUniqueId();
    }

    public static void eventClickHotlistProductFeatured(Context context, Hotlist hotlist) {
        getGTMEngine(context).eventClickHotlistProductFeatured(hotlist);
    }

    public static void eventImpressionHotlistProductFeatured(Context context, Hotlist hotlist) {
        getGTMEngine(context).eventImpressionHotlistProductFeatured(hotlist);
    }

    public static void impressionHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).impressionHotlistTracking(hotlistName, promoName, promoCode);
    }

    public static void clickCopyButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).clickCopyButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void clickTnCButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).clickTncButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void eventClearEnhanceEcommerce(Context context) {
        getGTMEngine(context).clearEnhanceEcommerce();
    }

    public static void eventTrackingEnhancedEcommerce(Context context, Map<String, Object> trackingData) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventTrackingEnhancedEcommerce(trackingData);

    }

    public static void eventImpressionPromoList(Context context, List<Object> list, String promoName) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventImpressionPromoList(list, promoName);
    }

    public static void eventClickPromoListItem(Context context, List<Object> list, String promoName) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventClickPromoListItem(list, promoName);
    }


    public static void eventCategoryLifestyleImpression(Context context, List<Object> list) {
        getGTMEngine(context).eventImpressionCategoryLifestyle(list);
    }

    public static void eventCategoryLifestyleClick(Context context, String categoryUrl, List<Object> list) {
        getGTMEngine(context).eventClickCategoryLifestyle(categoryUrl, list);
    }
}

