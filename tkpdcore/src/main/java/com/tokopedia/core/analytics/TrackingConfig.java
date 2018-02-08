package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.container.IPerformanceMonitoring;
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
     * Get MoEngage Engine Instance
     * @return MoEngage Instance
     */
    static IMoengageContainer getMoEngine(){
        return Jordan.init(MainApplication.getAppContext()).getMoEngageContainer();
    }

    /**
     * Get Firebase Performance Monitoring Engine Instance
     * @return FPM Instance
     */
    static IPerformanceMonitoring getFPMEngine(String traceName){
        return Jordan.init(MainApplication.getAppContext()).getFirebasePerformanceContainer(traceName);
    }

    /**
     * Initialize container to start at first time apps launched
     * @param what type container (GTM, Appsflyer, MoEngage)
     */
    public static void runFirstTime(AnalyticsKind what){
        switch (what){
            case GTM:
                getGTMEngine().loadContainer();
                break;
            case APPSFLYER:
                Jordan.init(MainApplication.getAppContext()).runFirstTimeAppsFlyer(SessionHandler.isV4Login(MainApplication.getAppContext()) ? SessionHandler.getLoginID(MainApplication.getAppContext()) : "00000");
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
    }
}
