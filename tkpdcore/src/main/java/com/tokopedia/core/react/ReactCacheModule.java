package com.tokopedia.core.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheModule extends ReactContextBaseJavaModule {
    public ReactCacheModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return null;
    }
}
