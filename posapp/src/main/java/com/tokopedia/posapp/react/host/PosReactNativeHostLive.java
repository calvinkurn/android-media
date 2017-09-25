package com.tokopedia.posapp.react.host;

import android.app.Application;

import com.facebook.react.ReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.core.util.GlobalConfig;

import java.util.List;

/**
 * Created by okasurya on 9/25/17.
 */

public class PosReactNativeHostLive extends PosReactNativeHost {

    public PosReactNativeHostLive(Application application) {
        super(application);
    }

    @Override
    public boolean getUseDeveloperSupport() {
        return GlobalConfig.isAllowDebuggingTools();
    }

    @Override
    protected List<ReactPackage> getPackages() {
        return getListPackages(getApplication());
    }

    @Override
    protected String getJSBundleFile() {
        return CodePush.getJSBundleFile();
    }
}
