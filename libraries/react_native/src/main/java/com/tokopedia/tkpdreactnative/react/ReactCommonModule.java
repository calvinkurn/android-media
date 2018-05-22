package com.tokopedia.tkpdreactnative.react;

import android.content.Context;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;

/**
 * @author ricoharisin .
 */

public class ReactCommonModule extends ReactContextBaseJavaModule {

    public Context context;

    public ReactCommonModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "CommonModule";
    }

    @ReactMethod
    public void getImageHost(Promise promise) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        promise.resolve(remoteConfig.getString(TkpdCache.RemoteConfigKey.IMAGE_HOST, "http://ecs7.tokopedia.net"));
    }
}
