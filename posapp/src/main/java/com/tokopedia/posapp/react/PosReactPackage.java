package com.tokopedia.posapp.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.tokopedia.posapp.react.reactmodule.DataReactModule;
import com.tokopedia.posapp.react.reactmodule.ProductDiscoveryReactModule;
import com.tokopedia.posapp.react.reactmodule.SessionReactModule;
import com.tokopedia.posapp.react.reactmodule.UtilReactModule;

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
        modules.add(new DataReactModule(reactContext));
        modules.add(new SessionReactModule(reactContext));
        modules.add(new ProductDiscoveryReactModule(reactContext));
        modules.add(new UtilReactModule(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
