package com.tokopedia.core.analytics;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.localytics.android.MessagingListener;
import com.localytics.android.PlacesCampaign;
import com.localytics.android.PushCampaign;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;
import com.tokopedia.core.analytics.container.ILocalyticsContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author  by alvarisi on 10/26/16.
 */

public abstract class TrackingConfig {

    public enum AnalyticsKind {
        GTM,
        APPSFLYER,
        LOCALYTICS,
        MOENGAGE
    }

    /**
     * Get GTM Container Instance
     * @return GTM Container
     */
    static IGTMContainer getGTMEngine(){
        return Nishikino.init(MainApplication.getAppContext()).startAnalytics();
    }

    /**
     * Get Appsflyer Container Instance
     * @return Appsflyer Instance
     */
    static IAppsflyerContainer getAFEngine(){
        return Jordan.init(MainApplication.getAppContext()).getAFContainer();
    }

    /**
     * Get Localytics Engine Instance
     * @return Localytics Instance
     */
    static ILocalyticsContainer getLocaEngine(){
        return Jordan.init(MainApplication.getAppContext()).getLocalyticsContainer();
    }

    /**
     * Get MoEngage Engine Instance
     * @return MoEngage Instance
     */
    static IMoengageContainer getMoEngine(){
        return Jordan.init(MainApplication.getAppContext()).getMoEngageContainer();
    }

    /**
     * Initialize container to start at first time apps launched
     * @param what type container (GTM, Appsflyer, Localytics)
     */
    public static void runFirstTime(AnalyticsKind what){
        CommonUtils.dumper("runfirstime");
        switch (what){
            case GTM:
                getGTMEngine().loadContainer();
                break;
            case APPSFLYER:
                Jordan.init(MainApplication.getAppContext()).runFirstTimeAppsFlyer(SessionHandler.isV4Login(MainApplication.getAppContext()) ? SessionHandler.getLoginID(MainApplication.getAppContext()) : "00000");
                break;
            case LOCALYTICS:
                Jordan.init(MainApplication.getAppContext()).getLocalyticsContainer().register((Application) MainApplication.getAppContext(), "673352445777", new MessagingListener() {
                    @Override
                    public void localyticsWillDisplayInAppMessage() {
                        CommonUtils.dumper("LocalyticsWillDisplayInAppMessage");
                    }

                    @Override
                    public void localyticsDidDisplayInAppMessage() {
                        CommonUtils.dumper("localyticsDidDisplayInAppMessage");
                    }

                    @Override
                    public void localyticsWillDismissInAppMessage() {
                        CommonUtils.dumper("localyticsWillDismissInAppMessage");
                    }

                    @Override
                    public void localyticsDidDismissInAppMessage() {
                        CommonUtils.dumper("localyticsDidDismissInAppMessage");
                    }

                    @Override
                    public boolean localyticsShouldShowPushNotification(@NonNull PushCampaign pushCampaign) {
                        return false;
                    }

                    @Override
                    public boolean localyticsShouldShowPlacesPushNotification(@NonNull PlacesCampaign placesCampaign) {
                        return false;
                    }

                    @NonNull
                    @Override
                    public NotificationCompat.Builder localyticsWillShowPlacesPushNotification(@NonNull NotificationCompat.Builder builder, @NonNull PlacesCampaign placesCampaign) {
                        return builder;
                    }

                    @NonNull
                    @Override
                    public NotificationCompat.Builder localyticsWillShowPushNotification(@NonNull NotificationCompat.Builder builder, @NonNull PushCampaign pushCampaign) {
                        return builder;
                    }
                });
                break;
            case MOENGAGE:
                Jordan.init(MainApplication.getAppContext()).getMoEngageContainer().initialize();
                break;
        }
    }

    /**
     * Set Logging Debugging, Production please set to false
     * @param debugState state debugging
     */
    public static void enableDebugging(boolean debugState) {
        getGTMEngine().getTagManager().setVerboseLoggingEnabled(debugState);
        getLocaEngine().setDebugging(debugState);
    }
}
