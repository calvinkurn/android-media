package com.tokopedia.posapp.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.tokopedia.posapp.react.nativemodules.PosCacheRNModule;
import com.tokopedia.posapp.react.nativemodules.ProductDiscoveryRNModule;
import com.tokopedia.posapp.react.nativemodules.SessionRNModule;
import com.tokopedia.posapp.react.nativemodules.UtilRNModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by okasurya on 8/29/17.
 */

public class PosReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new PosCacheRNModule(reactContext));
        modules.add(new SessionRNModule(reactContext));
        modules.add(new ProductDiscoveryRNModule(reactContext));
        modules.add(new UtilRNModule(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
