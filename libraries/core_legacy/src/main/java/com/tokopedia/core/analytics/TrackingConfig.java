package com.tokopedia.core.analytics;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;
import com.tokopedia.core.analytics.container.IMoengageContainer;
import com.tokopedia.core.analytics.nishikino.singleton.ContainerHolderSingleton;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * @author by alvarisi on 10/26/16.
 */

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
        new initGTMTask(application).execute();
    }

    static abstract class ContextAsyncTask extends AsyncTask<Void, Void, Void> {
        WeakReference<Context> contextWeakReference;

        ContextAsyncTask(Context context) {
            this.contextWeakReference = new WeakReference<>(context);
        }

        abstract void doInBackground(Context context);

        @Override
        protected final Void doInBackground(Void... voids) {
            Context context = null;
            if (this.contextWeakReference!= null && contextWeakReference.get()!= null) {
                context = this.contextWeakReference.get();
            }
            if (context == null) {
                return null;
            }
            doInBackground(context);
            return null;
        }
    }

    static class initGTMTask extends ContextAsyncTask {

        initGTMTask(Context context) {
            super(context);
        }

        @Override
        void doInBackground(Context context) {
            GTMContainer.newInstance(context).loadContainer();
        }
    }

    public static void runAppsFylerFirstTime(Application application) {
        new initAppsFlyerTask(application).execute();
    }

    static class initAppsFlyerTask extends ContextAsyncTask {

        initAppsFlyerTask(Context context) {
            super(context);
        }

        @Override
        void doInBackground(Context context) {
            SessionHandler sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
            Jordan.init(context).runFirstTimeAppsFlyer(sessionHandler.isV4Login() ? sessionHandler.getLoginID() : "00000");
        }
    }

    public static void runMoengageFirstTime(Application application) {
        new initMoengageTask(application).execute();
    }

    static class initMoengageTask extends ContextAsyncTask {

        initMoengageTask(Context context) {
            super(context);
        }

        @Override
        void doInBackground(Context context) {
            Jordan.init(context).getMoEngageContainer().initialize();
            SessionHandler sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
            TrackingUtils.setMoEngageExistingUser(context, sessionHandler.isLoggedIn());
        }
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
