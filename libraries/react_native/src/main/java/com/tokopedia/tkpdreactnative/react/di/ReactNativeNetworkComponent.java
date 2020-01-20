package com.tokopedia.tkpdreactnative.react.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdreactnative.react.ReactNetworkModule;

import dagger.Component;

/**
 * Created by alvarisi on 9/14/17.
 */
@ReactNativeNetworkScope
@Component(modules = ReactNativeNetworkModule.class, dependencies = BaseAppComponent.class)
public interface ReactNativeNetworkComponent {

    void inject(ReactNetworkModule reactNetworkModule);
}
