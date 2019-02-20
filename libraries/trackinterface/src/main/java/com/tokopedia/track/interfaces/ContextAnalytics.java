package com.tokopedia.track.interfaces;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

public abstract class ContextAnalytics implements Analytics {
    protected final Application context;

    public ContextAnalytics(Context context) {
        this.context = getApplication(context);
    }

    public Application getContext() {
        return context;
    }

    private Application getApplication(Context context) {
        Application application = null;
        if (context instanceof Activity) {
            application = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            application = ((Service) context).getApplication();
        } else if (context instanceof Application) {
            application = (Application) context;
        }

        return application;
    }

    public String TAG() {
        return "TEST";
    }

    public void initialize() {

    }
}
