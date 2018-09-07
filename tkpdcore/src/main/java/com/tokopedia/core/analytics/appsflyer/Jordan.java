package com.tokopedia.core.analytics.appsflyer;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.appsflyer.AppsFlyerConversionListener;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.container.IPerformanceMonitoring;
import com.tokopedia.core.analytics.container.MoEngageContainer;
import com.tokopedia.core.analytics.container.PerfMonContainer;

import java.util.Map;

/**
 * Created by Hafizh Herdi on 2/11/2016.
 *
 * This class is a library wrapper for AppsFlyer Analytics
 * Name taken from this https://en.wikipedia.org/wiki/Jordan
 *
 */
public class Jordan {

    private Context context;
    public static final String GCM_PROJECT_NUMBER = "692092518182";

    private Jordan(Context ctx){
        context = ctx;
    }

    public static Jordan init(Context context){
        return new Jordan(context);
    }

    public static Jordan init(Application application){
        return new Jordan(application.getApplicationContext());
    }

    public AppsflyerContainer runFirstTimeAppsFlyer(String userID){
        AppsflyerContainer appsflyerContainer = AppsflyerContainer.newInstance(context);
        CommonUtils.dumper("Appsflyer login userid " + userID);

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                // @TODO
            }

            @Override
            public void onInstallConversionFailure(String s) {
                // @TODO
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                // @TODO
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            CommonUtils.dumper("Error key Appsflyer");
            appsflyerContainer.initAppsFlyer(AppsflyerContainer.APPSFLYER_KEY, userID, conversionListener);
        }
        return appsflyerContainer;
    }

    public IAppsflyerContainer getAFContainer(){
        return AppsflyerContainer.newInstance(context);
    }

    public IMoengageContainer getMoEngageContainer() {
        return MoEngageContainer.getMoEngageContainer(context);
    }

    public IPerformanceMonitoring getFirebasePerformanceContainer(String traceName) {
        return PerfMonContainer.initTraceInstance(traceName);
    }

    public static final String AF_SCREEN_HOME_HOTLIST = "home_hotlist";
    public static final String AF_SCREEN_HOME_MAIN = "home_beranda";
    public static final String AF_SCREEN_PRODUCT_FEED = "home_productfeed";
    public static final String AF_SCREEN_FAVORIT = "home_favorit";
    public static final String AF_SCREEN_CAT = "productCategory";
    public static final String AF_SCREEN_PRODUCT = "productView";
    public static final String AF_SCREEN_WISHLIST = "wishList";
    public static final String AF_SCREEN_CART = "cart";

    public static final String AF_KEY_CATEGORY_ID = "c";
    public static final String AF_KEY_CATEGORY_NAME = "cn";
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

}
