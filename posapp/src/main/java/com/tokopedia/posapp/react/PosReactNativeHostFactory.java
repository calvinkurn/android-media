package com.tokopedia.posapp.react;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
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
        };
    }

    private List<ReactPackage> getListPackages(Application application) {
        return Arrays.asList(
                new MainReactPackage(),
                new CoreReactPackage(),
                new PosReactPackage(),
                new CodePush(getCodePushDeploymentKey(), application, GlobalConfig.isAllowDebuggingTools())
        );
    }

    private static String getCodePushDeploymentKey() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            return "RJQQF4Z7msN2YLRUIMdBj66oUUQ42b330ab3-6286-4793-b2b8-1067ed266709";
        }else {
            return "6H1Wtins9JpnmlDljcgMvBwEcpvA2b330ab3-6286-4793-b2b8-1067ed266709";
        }
    }
}
