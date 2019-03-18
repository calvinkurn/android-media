package com.tokopedia.recentview.analytics;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;

import java.util.List;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

public class RecentViewTracking {

    protected static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String LOGIN_ID = "LOGIN_ID";

    public static void trackEventClickOnProductRecentView(Context context,
                                                          Object dataItem) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "recent view",
                        "eventAction", "click on product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/recent"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        ),
                        "userId", getLoginID(context)
                )
        );
    }

    public static void trackEventImpressionOnProductRecentView(Context context,
                                                         List<Object> dataItemList) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "recent view",
                        "eventAction", "impression on product",
                        "eventLabel", "",
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        dataItemList.toArray(new Object[dataItemList.size()])
                                )),
                        "userId", getLoginID(context)
                )
        );
    }

    public static String getLoginID(Context context) {
        String u_id;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, "");
        return u_id;
    }
}
