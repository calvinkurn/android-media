package com.tokopedia.tkpdreactnative.react.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpdreactnative.react.ReactNetworkModule;

import dagger.Component;

/**
 * Created by alvarisi on 9/14/17.
 */
@ReactNativeNetworkScope
@Component(modules = ReactNativeNetworkModule.class, dependencies = AppComponent.class)
public interface ReactNativeNetworkComponent {

    void inject(ReactNetworkModule reactNetworkModule);
}
