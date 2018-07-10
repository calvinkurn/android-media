package com.tokopedia.posapp.react.di.component;

import com.tokopedia.posapp.PosRouterApplication;
import com.tokopedia.posapp.di.component.PosAppComponent;
import com.tokopedia.posapp.react.di.module.PosReactNativeModule;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeScope;

import dagger.Component;

@ReactNativeScope
@Component(modules = PosReactNativeModule.class, dependencies = PosAppComponent.class)
public interface PosReactNativeComponent {
    void inject(PosRouterApplication reactApplication);
}
