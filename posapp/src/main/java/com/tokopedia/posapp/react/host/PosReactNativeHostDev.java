package com.tokopedia.posapp.react.host;

import android.app.Application;

import com.facebook.react.ReactPackage;

import java.util.List;

/**
 * Created by okasurya on 9/25/17.
 */

public class PosReactNativeHostDev extends PosReactNativeHost {
    public PosReactNativeHostDev(Application application) {
        super(application);
    }

    @Override
    public boolean getUseDeveloperSupport() {
        return true;
    }

    @Override
    protected String getJSBundleFile() {
        return "index.android.bundle";
    }

    @Override
    protected String getJSMainModuleName() {
        return "reactscript/index.android";
    }


    @Override
    protected List<ReactPackage> getPackages() {
        return getListPackages(getApplication());
    }
}
