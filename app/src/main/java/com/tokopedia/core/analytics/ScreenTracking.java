package com.tokopedia.core.analytics;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * This Class for screen tracking
 */

public class ScreenTracking extends TrackingUtils {

    private static final String TAG = ScreenTracking.class.getSimpleName();

    public static void screen(String screen){
        if(TextUtils.isEmpty(screen)){
            try {
                throw new Exception("Fragment ScreenName cannot null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        getGTMEngine()
                .sendScreen(screen);
    }

    public static void screen(android.support.v4.app.Fragment fragment){
        getGTMEngine()
                .sendScreen(AppScreen.convertFragmentScreen(fragment));
    }

    public static void screenLoca(String screenName){
        getLocaEngine().tagScreen(screenName);
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

    public static void sendLocaProductDetailEvent(ProductDetailData successResult, Map<String,String> attributes){
        getLocaEngine().sendEventProductView(
                successResult.getInfo().getProductName(),
                Integer.toString(successResult.getInfo().getProductId()),
                successResult.getInfo().getProductCatalogName(),
                attributes
        );
    }

    public static void sendLocaCartEvent(Map<String, String> attributes){
        String screenName = MainApplication.getAppContext().getString(R.string.cart_pg_1);
        eventLoca(screenName, attributes);
        screenLoca(screenName);
        eventLocaInAppMessaging("event : Viewed " + screenName);

    }

    public static void sendAFPDPEvent(final ProductDetailData data, final String eventName){
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

                CommonUtils.dumper(TAG + "Appsflyer data " + adsID + " " + productID + " " + productPrice);
                getAFEngine().sendTrackEvent(eventName, values);
            }

            @Override
            public void onErrorAFAdsID() {
                CommonUtils.dumper(TAG + "Appsflyer error cant get advertisment ID");
            }
        });
    }

    public static void eventDiscoveryScreenAuth(){
        getGTMEngine().sendScreenAuthenticated(AppScreen.SCREEN_BROWSE_PRODUCT_FROM_CATEGORY);
    }
}
