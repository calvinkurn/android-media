package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.track.TrackApp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

@Deprecated
public class TrackingUtils{
    public static void eventCampaign(Context context, Campaign campaign) {
        Campaign temp = new Campaign(campaign);
        TrackApp.getInstance().getGTM().pushEvent("campaignTrack", campaign.getCampaign());
        TrackApp.getInstance().getGTM().sendGeneralEvent(campaign.getNullCampaignMap());
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
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.CLICK_MAIN_CATEGORY_ICON);
    }

    public static void sendMoEngageFavoriteEvent(Context context, String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.SHOP_NAME, shopName,
                AppEventTracking.MOENGAGE.SHOP_ID, shopID,
                AppEventTracking.MOENGAGE.SHOP_LOCATION, shopLocation,
                AppEventTracking.MOENGAGE.SHOP_URL_SLUG, shopDomain,
                AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, isShopOfficaial
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value,
                isFollowed ?
                        AppEventTracking.EventMoEngage.SELLER_ADDED_FAVORITE :
                        AppEventTracking.EventMoEngage.SELLER_REMOVE_FAVORITE);
    }

    public static void sendMoEngageReferralShareEvent(Context context,String channel) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CHANNEL, channel
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.REFERRAL_SHARE_EVENT);
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
        TrackApp.getInstance().getGTM()
                .eventError(className, errorMessage);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventOnline(Context context,String uid) {
        TrackApp.getInstance().getGTM()
                .eventOnline(uid);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventPushUserID(Context context,String userId) {
        TrackApp.getInstance().getGTM()
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
        TrackApp.getInstance().getGTM().sendGeneralEvent(dataLayers);
    }

    public static void sendAppsFlyerDeeplink(Activity activity) {
        TrackApp.getInstance().getAppsFlyer().sendDeeplinkData(activity);
    }

    public static String getClientID(Context context) {
        return TrackApp.getInstance().getGTM().getClientIDString();
    }

    public static String getAfUniqueId(Context context) {
        return TrackApp.getInstance().getAppsFlyer().getUniqueId();
    }

    public static void eventClickHotlistProductFeatured(Context context, Hotlist hotlist) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "eventCategory", AppEventTracking.Category.CATEGORY_HOTLIST,
                        "eventAction", String.format("feature product hotlist %s - click product %s", hotlist.getHotlistAlias(), hotlist.getProductList().get(0).getProductName()),
                        "eventLabel", String.format("%s - %s", hotlist.getScreenName(), hotlist.getPosition(),
                                "ecommerce", DataLayer.mapOf(
                                        "click", DataLayer.mapOf(
                                                "actionField", DataLayer.mapOf(
                                                        "list", "hotlist"),
                                                "products", hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()])
                                        )
                                )
                        )
                ));
    }

    public static void eventImpressionHotlistProductFeatured(Context context, Hotlist hotlist) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "ecommerce", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "hotlist"),
                                "impressions",
                                DataLayer.listOf(
                                        hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()]))
                        )
                )
        );
    }

    public static void impressionHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo impression",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public static void clickCopyButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click salin kode",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public static void clickTnCButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click syarat ketentuan",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public static void eventTrackingEnhancedEcommerce(Context context, Map<String, Object> trackingData) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(trackingData);
    }

    public static void eventImpressionPromoList(Context context, List<Object> list, String promoName) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickPromoListItem(Context context, List<Object> list, String promoName) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }


    public static void eventCategoryLifestyleImpression(Context context, List<Object> list) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", "promoView",
                        "eventCategory", "category page",
                        "eventAction", "subcategory impression",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()]))))
                )
        );
    }

    public static void eventCategoryLifestyleClick(Context context, String categoryUrl, List<Object> list) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", "promoClick",
                        "eventCategory", "category page",
                        "eventAction", "click subcategory",
                        "eventLabel", categoryUrl,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()])))),
                        "destinationURL", categoryUrl
                )
        );
    }
}

