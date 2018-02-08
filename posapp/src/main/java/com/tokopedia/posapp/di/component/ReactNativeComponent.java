package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.PosRouterApplication;
import com.tokopedia.posapp.di.module.PosReactNativeModule;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeScope;

import dagger.Component;

@ReactNativeScope
@Component(modules = PosReactNativeModule.class, dependencies = AppComponent.class)
public interface ReactNativeComponent {
    void inject(PosRouterApplication reactApplication);
}
