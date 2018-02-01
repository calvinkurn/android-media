package com.tokopedia.posapp.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.tkpdreactnative.react.CoreReactPackage;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.Arrays;
import java.util.List;

/**
 * Created by okasurya on 8/29/17.
 */

public class PosReactNativeHostFactory {
    private static PosReactNativeHostFactory instance;

    protected PosReactNativeHostFactory() {}

    public static ReactNativeHost init(Application application) {
        if(instance == null) instance = new PosReactNativeHostFactory();

        return instance.createReactNativeHost(application);
    }

    private ReactNativeHost createReactNativeHost(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            public boolean getUseDeveloperSupport() {
                return GlobalConfig.isAllowDebuggingTools();
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return getListPackages(application);
            }

            @Override
            protected String getJSBundleFile() {
                return CodePush.getJSBundleFile();
            }
        };
    }

    private ReactNativeHost createReactNativeHostDev(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            public boolean getUseDeveloperSupport() {
                return true;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return getListPackages(application);
            }

            @Override
            protected String getJSBundleFile() {
                return "index.android.bundle";
            }

            @Override
            protected String getJSMainModuleName() {
                return "index.android";
            }
        };
    }

    protected List<ReactPackage> getListPackages(Application application) {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new CoreReactPackage(),
                new PosReactPackage(),
                new CodePush(ReactConst.CODE_PUSH_DEPLOYMENT_KEY, application, GlobalConfig.isAllowDebuggingTools())
        );
    }
}
