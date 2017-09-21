package com.tokopedia.tkpd.react;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeModule;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeScope;

import dagger.Component;

/**
 * @author  by alvarisi on 9/14/17.
 */
@ReactNativeScope
@Component(modules = ReactNativeModule.class, dependencies = AppComponent.class)
public interface ReactNativeComponent {

    void inject(ConsumerRouterApplication reactApplication);
}
