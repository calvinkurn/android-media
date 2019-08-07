package com.tokopedia.tkpdreactnative.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.tokopedia.tkpdreactnative.react.banner.ReactBannerManager;
import com.tokopedia.tkpdreactnative.react.banner.ReactBannerManager2;
import com.tokopedia.tkpdreactnative.react.image.RctImageManager;
import com.tokopedia.tkpdreactnative.react.image.RctImageRemoteManager;
import com.tokopedia.tkpdreactnative.react.lineargradient.LinearGradientManager;
import com.tokopedia.tkpdreactnative.react.viewpager.ReactViewPagerManager;
import com.tokopedia.tkpdreactnative.react.youtube.YouTubeManager;
import com.tokopedia.tkpdreactnative.react.youtube.YouTubeModule;

import java.util.ArrayList;
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
        return Arrays.asList(
                new YouTubeManager(),
                new ReactBannerManager(),
                new ReactBannerManager2(),
                new LinearGradientManager(),
                new ButtonViewManager(),
                new ReactImageManager(),
                new RctImageManager(),
                new RctImageRemoteManager(),
                new ReactViewPagerManager()
        );
    }
}
