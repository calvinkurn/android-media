package com.tokopedia.tracking_debugger;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.tracking_debugger.ui.TrackingDebuggerActivity;

/**
 * @author okasurya on 5/14/18.
 */

public class TrackingLogger implements Logger {
    private static Logger instance;

    public static Logger getInstance() {
        if(instance == null) instance = new TrackingLogger();

        return instance;
    }

    private TrackingLogger() {}

    @Override
    public void save(String data, String name) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public Intent getLaunchIntent(Context context) {
        return new Intent(context, TrackingDebuggerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
