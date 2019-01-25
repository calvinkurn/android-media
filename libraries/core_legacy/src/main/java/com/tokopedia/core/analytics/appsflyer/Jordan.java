package com.tokopedia.core.analytics.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerConversionListener;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.container.AppsflyerAnalytics;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.container.MoEngageContainer;

import java.util.Map;

/**
 * Created by Hafizh Herdi on 2/11/2016.
 * <p>
 * This class is a library wrapper for AppsFlyer Analytics
 * Name taken from this https://en.wikipedia.org/wiki/Jordan
 */
public class Jordan {

    private Application context;
    public static final String GCM_PROJECT_NUMBER = "692092518182";
    private static boolean isAppsflyerCallbackHandled;

    private Jordan(Context ctx) {
        Application application = null;
        if (ctx instanceof Activity) {
            application = ((Activity) ctx).getApplication();
        } else if (ctx instanceof Service) {
            application = ((Service) ctx).getApplication();
        } else if (ctx instanceof Application) {
            application = (Application) ctx;
        }
        this.context = application;
    }

    private Jordan(Application application) {
        this.context = application;
    }

    public static Jordan init(Context context) {
        return new Jordan(context);
    }

    public static Jordan init(Application application) {
        return new Jordan(application);
    }

    /**
     * latest release codes. {@link AppsflyerAnalytics#initialize()}
     * @param userID
     * @return
     */
    @Deprecated
    public AppsflyerContainer runFirstTimeAppsFlyer(String userID){
        if(context == null){
            return null;
        }
        AppsflyerContainer appsflyerContainer = AppsflyerContainer.newInstance(context);
        CommonUtils.dumper("Appsflyer login userid " + userID);

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                if (isAppsflyerCallbackHandled) return;
                isAppsflyerCallbackHandled = true;

                try {
                    //get first launch and deeplink
                    String isFirstLaunch = conversionData.get("is_first_launch");
                    String deeplink = conversionData.get("af_dp");

                    if (!TextUtils.isEmpty(isFirstLaunch) && isFirstLaunch.equalsIgnoreCase("true") && !TextUtils.isEmpty(deeplink)) {
                        //open deeplink
                        AppsflyerContainer.setDefferedDeeplinkPathIfExists(deeplink);
                    }
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onInstallConversionFailure(String s) {
                // @TODO
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {
                // @TODO
            }
        };

        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            appsflyerContainer.initAppsFlyer(bundle.getString(AppEventTracking.AF.APPSFLYER_KEY), userID, conversionListener);
            ((TkpdCoreRouter)(context.getApplicationContext())).onAppsFlyerInit();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            CommonUtils.dumper("Error key Appsflyer");
            appsflyerContainer.initAppsFlyer(AppsflyerContainer.APPSFLYER_KEY, userID, conversionListener);
        }
        return appsflyerContainer;
    }

    public IAppsflyerContainer getAFContainer(){
        if(context != null)
            return AppsflyerContainer.newInstance(application);
        else
            return null;
    }

    public IMoengageContainer getMoEngageContainer() {
        if(context != null)
            return MoEngageContainer.getMoEngageContainer(application);
        else
            return null;
    }

    public static final String AF_SCREEN_HOME_HOTLIST = "home_hotlist";
    public static final String AF_SCREEN_HOME_MAIN = "home_beranda";
    public static final String AF_SCREEN_PRODUCT_FEED = "home_productfeed";
    public static final String AF_SCREEN_FAVORIT_CACHE_AF_KEY_ALL_PRODUCTS= "home_favorit";
    public static final String AF_SCREEN_CAT = "productCategory";
    public static final String AF_SCREEN_PRODUCT = "productView";
    public static final String AF_SCREEN_WISHLIST = "wishList";
    public static final String AF_SCREEN_CART = "cart";
    public static final String AF_SHIPPING_PRICE = "af_shipping_price";
    public static final String AF_PAYMENT_ID = "af_payment_id";
    public static final String AF_PURCHASE_SITE = "af_purchase_site";
    public static final String AF_SHOP_ID = "af_shop_id";

    public static final String AF_KEY_CATEGORY_ID = "c";
    public static final String AF_KEY_CATEGORY_NAME = "category";
    public static final String AF_KEY_PRODUCT_ID = "id";
    public static final String AF_KEY_OS_TYPE = "os";
    public static final String AF_KEY_FINALPRICE = "sprc";
    public static final String AF_KEY_ADS_ID = "advertising_id";
    public static final String AF_KEY_CRITEO = "criteo_track_transaction";

    public static final String CACHE_AF_KEY_JSONIDS = "af_json_ids";
    public static final String CACHE_AF_KEY_QTY = "af_qty";
    public static final String CACHE_AF_KEY_ALL_PRODUCTS = "af_allprod";
    public static final String CACHE_AF_KEY_REVENUE = "af_revs";
    public static final String CACHE_LC_KEY_ALL_PRODUCTS = "lc_allprod";
    public static final String CACHE_LC_KEY_SHIPPINGRATE = "lc_shippingrate";
    public static final String CACHE_KEY_DATA_AR_ALLPURCHASE = "cc_purchase";
    public static final String CACHE_KEY_DATA_CHECKOUT = "cc_checkout";

    public static final String AF_VALUE_PRODUCTTYPE = "product";
    public static final String AF_VALUE_PRODUCT_TYPE = "productType";
    public static final String AF_VALUE_PRODUCTGROUPTYPE = "product_group";
    public static final String VALUE_ANDROID ="Android" ;
    public static final String VALUE_IDR ="IDR" ;

}
