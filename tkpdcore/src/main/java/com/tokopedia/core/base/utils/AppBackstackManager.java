package com.tokopedia.core.base.utils;

/**
 * Created by alvarisi on 11/2/17.
 */

public class AppBackstackManager {
    private static AppBackstackManager appBackstackManager;
    public AppBackstackManager() {
    }

    public static AppBackstackManager newInstance(){
        if (appBackstackManager == null)
            appBackstackManager = new AppBackstackManager();
        return appBackstackManager;
    }


}
