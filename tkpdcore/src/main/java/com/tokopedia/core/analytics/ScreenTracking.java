package com.tokopedia.core.analytics;

import android.app.Activity;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by Herdi_WORK on 25.10.16.
 *         modified by Alvarisi
 *         This Class for screen tracking
 */

public class ScreenTracking extends TrackingUtils {

    private static final String TAG = ScreenTracking.class.getSimpleName();

    public interface IOpenScreenAnalytics {
        String getScreenName();
    }

    public static void sendScreen(Activity activity, IOpenScreenAnalytics openScreenAnalytics) {
        try {
            ScreenTrackingBuilder
                    .newInstance(activity, openScreenAnalytics, getAfUniqueId())
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void screen(String screen) {
        if (TextUtils.isEmpty(screen)) {
            screen = "Default Fragment Name";
        }

        getGTMEngine().sendScreen(screen);
    }

    public static void sendAFGeneralScreenEvent(String screenName) {
        Map<String, Object> afValue = new HashMap<>();
        afValue.put(AFInAppEventParameterName.DESCRIPTION, screenName);
        getAFEngine().sendTrackEvent(AFInAppEventType.CONTENT_VIEW, afValue);
    }

    public static void eventAuthScreen(Authenticated authenticated, String screenName) {
        getGTMEngine()
                .eventAuthenticate(authenticated)
                .sendScreen(screenName);
    }

    public static void sendAFPDPEvent(final ProductDetailData data, final String eventName) {
        final AnalyticsCacheHandler analHandler = new AnalyticsCacheHandler();
        getAFEngine().getAdsID(new AppsflyerContainer.AFAdsIDCallback() {
            @Override
            public void onGetAFAdsID(String adsID) {
                String productID = data.getInfo().getProductId() + "";
                String productPrice = CurrencyFormatHelper.convertRupiahToInt(data.getInfo().getProductPrice()) + "";

                Map<String, Object> values = new HashMap<>();
                values.put(Jordan.AF_KEY_ADS_ID, adsID);
                values.put(AFInAppEventParameterName.DESCRIPTION, data.getInfo().getProductName());
                values.put(AFInAppEventParameterName.CONTENT_ID, productID);
                values.put(AFInAppEventParameterName.PRICE, productPrice);
                values.put(AFInAppEventParameterName.CURRENCY, "IDR");
                values.put(Jordan.AF_KEY_CATEGORY_NAME,data.getBreadcrumb().get(0).getDepartmentName());
                if (!analHandler.isAdsIdAvailable()) {
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

    public static void eventDiscoveryScreenAuth(String departmentId) {
        if (!TextUtils.isEmpty(departmentId)) {
            getGTMEngine().sendScreenAuthenticated(
                    AppScreen.SCREEN_BROWSE_PRODUCT_FROM_CATEGORY + departmentId
            );
        }
    }

    public static void eventOfficialStoreScreenAuth(String shopID, String shopType, String pageType, String productId) {
        getGTMEngine().sendScreenAuthenticatedOfficialStore(
                AppScreen.SCREEN_PRODUCT_INFO, shopID, shopType, pageType, productId
        );
    }
}
