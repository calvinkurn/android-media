package com.tokopedia.tkpdreactnative.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.tokopedia.tkpdreactnative.react.youtube.YouTubeManager;
import com.tokopedia.tkpdreactnative.react.youtube.YouTubeModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * @author ricoharisin .
 */

public class CoreReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new ReactNetworkModule(reactContext));
        modules.add(new ReactNavigationModule(reactContext));
        modules.add(new ReactCommonModule(reactContext));
        modules.add(new YouTubeModule(reactContext));
        modules.add(new DeeplinkManager(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new YouTubeManager()
        );
    }
}
