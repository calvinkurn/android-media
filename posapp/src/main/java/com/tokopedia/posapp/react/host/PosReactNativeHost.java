package com.tokopedia.posapp.react.host;

import android.app.Application;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.posapp.react.PosReactPackage;
import com.tokopedia.tkpdreactnative.react.CoreReactPackage;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.util.Arrays;
import java.util.List;

/**
 * Created by okasurya on 9/25/17.
 */

public abstract class PosReactNativeHost extends ReactNativeHost {

    protected PosReactNativeHost(Application application) {
        super(application);
    }

    public ReactInstanceManager getReactInstanceManager() {
        return createReactInstanceManager();
    }

    @Override
    protected ReactInstanceManager createReactInstanceManager() {
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setJSMainModuleName(getJSMainModuleName())
                .setUseDeveloperSupport(getUseDeveloperSupport())
                .setRedBoxHandler(getRedBoxHandler())
                .setUIImplementationProvider(getUIImplementationProvider())
                .setInitialLifecycleState(LifecycleState.RESUMED);

        for (ReactPackage reactPackage : getPackages()) {
            builder.addPackage(reactPackage);
        }

        String jsBundleFile = getJSBundleFile();
        if (jsBundleFile != null) {
            builder.setJSBundleFile(jsBundleFile);
        } else {
            builder.setBundleAssetName(Assertions.assertNotNull(getBundleAssetName()));
        }
        ReactInstanceManager reactInstanceManager = builder.build();
        reactInstanceManager.createReactContextInBackground();
        return reactInstanceManager;
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
