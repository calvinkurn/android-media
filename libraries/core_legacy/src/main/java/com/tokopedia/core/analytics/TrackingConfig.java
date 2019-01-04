package com.tokopedia.core.analytics;

import android.app.Application;
import android.content.Context;

import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.deprecated.SessionHandler;

/**
 * @author  by alvarisi on 10/26/16.
 */

public abstract class TrackingConfig {

    public enum AnalyticsKind {
        GTM,
        APPSFLYER,
        MOENGAGE
    }

    /**
     * Get GTM Container Instance
     * @return GTM Container
     */
    static IGTMContainer getGTMEngine(Context context){
        return Nishikino.init(context).startAnalytics();
    }

    /**
     * Get Appsflyer Container Instance
     * @return Appsflyer Instance
     */
    static IAppsflyerContainer getAFEngine(Context context){
        return Jordan.init(context).getAFContainer();
    }

    /**
     * Get MoEngage Engine Instance
     * @return MoEngage Instance
     */
    static IMoengageContainer getMoEngine(Context context){
        return Jordan.init(context).getMoEngageContainer();
    }

    /**
     * Initialize container to start at first time apps launched
     * @param what type container (GTM, Appsflyer, MoEngage)
     */
    public static void runFirstTime(Application context, AnalyticsKind what, SessionHandler sessionHandler){
        switch (what){
            case GTM:
                getGTMEngine(context).loadContainer();
                break;
            case APPSFLYER:
                Jordan.init(context).runFirstTimeAppsFlyer(sessionHandler.isV4Login() ? sessionHandler.getLoginID() : "00000");
                break;
            case MOENGAGE:
                Jordan.init(context).getMoEngageContainer().initialize();
                break;
        }
    }

    /**
     * Set Logging Debugging, Production please set to false
     * @param debugState state debugging
     */
    public static void enableDebugging(Context context, boolean debugState) {
        getGTMEngine(context).getTagManager().setVerboseLoggingEnabled(debugState);
    }
}
