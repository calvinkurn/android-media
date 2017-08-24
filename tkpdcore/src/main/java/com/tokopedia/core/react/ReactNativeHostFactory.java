package com.tokopedia.core.react;

import android.app.Application;
import android.provider.Settings;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.GlobalConfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class ReactNativeHostFactory {


    public static ReactNativeHost init(Application application) {
        /*reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("reactscript/index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new CoreReactPackage())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();*/

        return createReactNativeHostDev(application);

    }

    private static ReactNativeHost createReactNativeHost(final Application application) {
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

    private static ReactNativeHost createReactNativeHostDev(final Application application) {
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
                return "reactscript/index.android";
            }
        };
    }

    private static List<ReactPackage> getListPackages(Application application) {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new CoreReactPackage(),
                new CodePush(ReactConst.CODE_PUSH_DEPLOYMENT_KEY, application, GlobalConfig.isAllowDebuggingTools())
        );
    }
}
