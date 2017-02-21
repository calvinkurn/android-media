package com.tokopedia.core;

import android.app.Activity;

import com.tokopedia.core.app.TkpdCoreRouter;

/**
 * Created by sebastianuskh on 1/9/17.
 */
public class Router {
    public static void clearEtalase(Activity activity){
        if(activity.getApplication() instanceof TkpdCoreRouter){
            ((TkpdCoreRouter)activity.getApplication()).clearEtalaseCache();
        }
    }
}
