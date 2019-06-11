package com.tokopedia.tkpd.timber;

import android.app.Application;

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.BuildConfig;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init();
 */
public class TimberWrapper {
    public static void init(Application application){
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        } else {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(application);

            Timber.plant(new TimberReportingTree());
        }
    }
}
