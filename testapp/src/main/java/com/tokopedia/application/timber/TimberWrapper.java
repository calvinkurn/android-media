package com.tokopedia.application.timber;

import com.tokopedia.tkpd.BuildConfig;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init();
 */
public class TimberWrapper {
    public static void init(){
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new TimberReportingTree());
        }
    }
}
