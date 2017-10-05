package com.tokopedia.core;

import android.app.Activity;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;

/**
 * Created by sebastianuskh on 1/9/17.
 */
public class Router {
    public static void clearEtalase(Activity activity){
        if(activity.getApplication() instanceof TkpdCoreRouter){
            ((TkpdCoreRouter)activity.getApplication()).clearEtalaseCache();
        }
    }

    public static void onLogout(Activity activity, AppComponent component) {
        if (activity.getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) activity.getApplication()).onLogout(component);
        }
    }
}
