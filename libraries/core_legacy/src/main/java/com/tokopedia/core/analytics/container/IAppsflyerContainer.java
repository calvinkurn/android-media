package com.tokopedia.core.analytics.container;

import android.app.Activity;

import com.appsflyer.AppsFlyerConversionListener;

import java.util.Map;

/**
 * @author by alvarisi on 10/27/16.
 */

public interface IAppsflyerContainer {
    void initAppsFlyer(String key, String userID);

    void initAppsFlyer(String key, String userID, AppsFlyerConversionListener conversionListener);

    void setUserID(String userID);

    /**
     * Method for send general event
     * @param eventName String event name
     * @param eventValue Maps Event Detail
     */
    void sendTrackEvent(String eventName, Map<String, Object> eventValue);

    /**
     * get adsvertising id
     * @param callback listener
     */
    void getAdsID(AppsflyerContainer.AFAdsIDCallback callback);

    String getAdsIdDirect();

    String getUniqueId();

    void sendDeeplinkData(Activity activity);
}