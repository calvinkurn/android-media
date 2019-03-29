package com.tokopedia.core.analytics;

import android.app.Application;
import android.content.Context;

import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;

/**
 * @author by alvarisi on 10/26/16.
 */
@Deprecated
public abstract class TrackingConfig {

    /**
     * Get GTM Container Instance
     *
     * @return GTM Container
     */
    static IGTMContainer getGTMEngine(Context context) {
        return GTMContainer.newInstance(context);
    }

    /**
     * Get Appsflyer Container Instance
     *
     * @return Appsflyer Instance
     */
    static IAppsflyerContainer getAFEngine(Context context) {
        return Jordan.init(context).getAFContainer();
    }

    /**
     * Get MoEngage Engine Instance
     *
     * @return MoEngage Instance
     */
    static IMoengageContainer getMoEngine(Context context) {
        return Jordan.init(context).getMoEngageContainer();
    }

    public static void runGTMFirstTime(Application application) {
        GTMContainer.newInstance(application).loadContainer();
    }

    public static void runAppsFylerFirstTime(Application application) {
        SessionHandler sessionHandler = RouterUtils.getRouterFromContext(application).legacySessionHandler();
        Jordan.init(application).runFirstTimeAppsFlyer(sessionHandler.isV4Login() ? sessionHandler.getLoginID() : "00000");
    }

    public static void runMoengageFirstTime(Application application) {
        Jordan.init(application).getMoEngageContainer().initialize();
        SessionHandler sessionHandler = RouterUtils.getRouterFromContext(application).legacySessionHandler();
        TrackingUtils.setMoEngageExistingUser(application, sessionHandler.isLoggedIn());
    }

    /**
     * Set Logging Debugging, Production please set to false
     *
     * @param debugState state debugging
     */
    public static void enableDebugging(Context context, boolean debugState) {
        getGTMEngine(context).getTagManager().setVerboseLoggingEnabled(debugState);
    }
}
