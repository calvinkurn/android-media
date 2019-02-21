package com.tokopedia.core.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.legacy.CommonUtils;
//import com.tkpd.library.utils.legacy.CurrencyFormatHelper;
import com.tkpd.library.utils.legacy.CurrencyFormatHelper;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
//import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * This Class for screen tracking
 */

public class ScreenTracking extends TrackingUtils {

    private static final String TAG = ScreenTracking.class.getSimpleName();

    public interface IOpenScreenAnalytics {
        String getScreenName();
    }

    public static void sendScreen(Activity activity, IOpenScreenAnalytics openScreenAnalytics) {
        try {
            ScreenTrackingBuilder
                    .newInstance(openScreenAnalytics)
                    .execute(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendScreen(Context context, String screenName) {
        try {
            ScreenTrackingBuilder
                    .newInstance(screenName)
                    .execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendScreen(Context context, String screenName, String deepLink) {
        try {
            ScreenTrackingBuilder
                    .newInstance(context, screenName, getAfUniqueId(context))
                    .setDeepLinkUrl(deepLink)
                    .execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void screen(Context context, String screen) {
        if (TextUtils.isEmpty(screen)) {
            return;
        }
        getGTMEngine(context).sendScreen(screen);
    }

    public static void sendAFGeneralScreenEvent(Context context, String screenName) {
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.DESCRIPTION, screenName);
        getAFEngine(context).sendTrackEvent(AFInAppEventType.CONTENT_VIEW, afValue);
    }

    public static void eventAuthScreen(Context context, Map<String, String> customDimension, String screenName) {
        getGTMEngine(context)
                .sendScreenAuthenticated(screenName, customDimension);
    }

    public static void sendAFPDPEvent(Context context, final ProductDetailData data, final String eventName) {
        if (context instanceof Application && context instanceof AbstractionRouter) {
            CacheManager cacheManager = ((AbstractionRouter) context).getGlobalCacheManager();
            final AnalyticsCacheHandler analHandler = new AnalyticsCacheHandler(cacheManager);
            getAFEngine(context).getAdsID(new AppsflyerContainer.AFAdsIDCallback() {
                @Override
                public void onGetAFAdsID(String adsID) {
                    String productID = data.getInfo().getProductId() + "";
                    String productPrice = CurrencyFormatHelper.convertRupiahToInt(data.getInfo().getProductPrice()) + "";

                    Map<String, Object> values = new HashMap<>();
                    values.put(Jordan.AF_KEY_ADS_ID, adsID);
                    values.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_PRODUCT);
                    values.put(AFInAppEventParameterName.CONTENT_ID, productID);
                    values.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
                    values.put(AFInAppEventParameterName.PRICE, productPrice);
                    values.put(AFInAppEventParameterName.CURRENCY, "IDR");
                    values.put(AFInAppEventParameterName.QUANTITY, 1);
                    if (!analHandler.isAdsIdAvailable()) {
                        analHandler.setAdsId(adsID);
                    }

                    CommonUtils.dumper(TAG + "Appsflyer data " + adsID + " " + productID + " " + productPrice);
                    getAFEngine(context).sendTrackEvent(eventName, values);
                }

                @Override
                public void onErrorAFAdsID() {
                    CommonUtils.dumper(TAG + "Appsflyer error cant get advertisment ID");
                }
            });
        }
    }

    public static void eventDiscoveryScreenAuth(Context context, String departmentId) {
        if (!TextUtils.isEmpty(departmentId)) {
            getGTMEngine(context).sendScreenAuthenticated(
                    AppScreen.SCREEN_BROWSE_PRODUCT_FROM_CATEGORY + departmentId
            );
        }
    }

    public static void eventOfficialStoreScreenAuth(Context context, String shopID, String shopType, String pageType, String productId) {
        getGTMEngine(context).sendScreenAuthenticated(
                AppScreen.SCREEN_PRODUCT_INFO, shopID, shopType, pageType, productId
        );
    }

    public static void eventCustomScreen(Context context, String screenName, String shopID, String shopType, String pageType, String productId) {
        getGTMEngine(context).sendScreenAuthenticated(screenName, shopID, shopType, pageType, productId);
    }

}
