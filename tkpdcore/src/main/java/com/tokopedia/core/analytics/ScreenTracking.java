package com.tokopedia.core.analytics;

import android.app.Activity;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * This Class for screen tracking
 */

public class ScreenTracking extends TrackingUtils {

    private static final String TAG = ScreenTracking.class.getSimpleName();
    private static final String AF_UNAVAILABLE_VALUE = "none";

    public interface IOpenScreenAnalytics {
        String getScreenName();
    }
    public static void sendScreen(Activity activity, IOpenScreenAnalytics openScreenAnalytics){
        ScreenTracking.OpenScreenTracking openScreenTracking = ScreenTracking
                .OpenScreenTracking
                .newInstance(activity, openScreenAnalytics);
        openScreenTracking.execute();
    }

    private static class OpenScreenTracking {
        private IOpenScreenAnalytics mOpenScreenAnalytics;
        Authenticated authEvent;
        Activity mActivity;

        public static OpenScreenTracking newInstance(Activity activity,
                                                     IOpenScreenAnalytics openScreenAnalytics) {
            return new OpenScreenTracking(activity, openScreenAnalytics);
        }

        OpenScreenTracking(Activity activity, IOpenScreenAnalytics openScreenAnalytics) {
            this.mOpenScreenAnalytics = openScreenAnalytics;
            this.mActivity = activity;
            authEvent = new Authenticated();
            authEvent.setUserFullName(SessionHandler.getLoginName(activity));
            authEvent.setUserID(SessionHandler.getGTMLoginID(activity));
            authEvent.setShopID(SessionHandler.getShopID(activity));
            authEvent.setUserSeller(SessionHandler.isUserHasShop(activity) ? 1 : 0);
            authEvent.setAfUniqueId(getAfUniqueId() != null? getAfUniqueId() : AF_UNAVAILABLE_VALUE);

            if(activity.getClass().getSimpleName().equals("ParentIndexHome")){
                authEvent.setNetworkSpeed(TrackingUtils.getNetworkSpeed(activity));
            }

        }

        public void execute() {
            if (mOpenScreenAnalytics == null || TextUtils.isEmpty(mOpenScreenAnalytics.getScreenName())) {
                ScreenTracking.eventAuthScreen(authEvent, this.mActivity.getClass().getSimpleName());
            } else {
                ScreenTracking.eventAuthScreen(authEvent, mOpenScreenAnalytics.getScreenName());
            }
        }
    }

    public static void screen(String screen){
        if(TextUtils.isEmpty(screen)){
            try {
                throw new Exception("Fragment ScreenName cannot null");
            } catch (Exception e) {
                e.printStackTrace();
                screen = "Default Fragment Name";
            }
        }

        getGTMEngine()
                .sendScreen(screen);
    }

    public static void sendAFGeneralScreenEvent(String screenName){
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.DESCRIPTION, screenName);
        getAFEngine().sendTrackEvent(AFInAppEventType.CONTENT_VIEW, afValue);
    }

    public static void eventAuthScreen(Authenticated authenticated, String screenName){
        getGTMEngine()
                .eventAuthenticate(authenticated)
                .sendScreen(screenName);
    }

    public static void sendAFPDPEvent(final ProductDetailData data, final String eventName){
        final AnalyticsCacheHandler analHandler = new AnalyticsCacheHandler();
        getAFEngine().getAdsID(new AppsflyerContainer.AFAdsIDCallback() {
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
                if(!analHandler.isAdsIdAvailable()){
                    analHandler.setAdsId(adsID);
                }

                CommonUtils.dumper(TAG + "Appsflyer data " + adsID + " " + productID + " " + productPrice);
                getAFEngine().sendTrackEvent(eventName, values);
            }

            @Override
            public void onErrorAFAdsID() {
                CommonUtils.dumper(TAG + "Appsflyer error cant get advertisment ID");
            }
        });
    }

    public static void eventDiscoveryScreenAuth(String departmentId){
        if(!TextUtils.isEmpty(departmentId)) {
            getGTMEngine().sendScreenAuthenticated(
                    AppScreen.SCREEN_BROWSE_PRODUCT_FROM_CATEGORY + departmentId
            );
        }
    }

    public static void eventOfficialStoreScreenAuth(String shopID, String shopType){
        getGTMEngine().sendScreenAuthenticatedOfficialStore(
                AppScreen.SCREEN_OFFICIAL_STORE, shopID, shopType
        );
    }
}
