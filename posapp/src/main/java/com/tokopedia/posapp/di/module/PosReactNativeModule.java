package com.tokopedia.posapp.di.module;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.tokopedia.posapp.react.PosReactNativeHostFactory;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/20/17.
 */
@Module
public class PosReactNativeModule {
    private Application reactApplication;
    public PosReactNativeModule(Application reactApplication) {
        this.reactApplication = reactApplication;
    }

    @Provides
    @ReactNativeScope
    ReactNativeHost provideReactNativeHostFactory() {
        return PosReactNativeHostFactory.init(reactApplication);
    }

    @Provides
    @ReactNativeScope
    ReactUtils provideReactUtils(ReactNativeHost reactNativeHost) {
        return ReactUtils.init(reactNativeHost.getReactInstanceManager());
    }
}
