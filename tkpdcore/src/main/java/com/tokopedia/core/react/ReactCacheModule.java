package com.tokopedia.core.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.react.di.ReactCacheDependencies;
import com.tokopedia.core.react.domain.ReactCacheRepository;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheModule extends ReactContextBaseJavaModule {
    ReactCacheRepository reactCacheRepository;

    public ReactCacheModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactCacheRepository = new ReactCacheDependencies(reactContext).provideReactCacheRepository();
    }

    @Override
    public String getName() {
        return null;
    }

    @ReactMethod
    public String getData(String tableName, String id) {
        return "";
    }

    @ReactMethod
    public String getDataList(String tableName, int start, int rows) {
        return "";
    }
}
