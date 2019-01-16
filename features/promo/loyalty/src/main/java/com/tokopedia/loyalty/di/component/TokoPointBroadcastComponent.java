package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.broadcastreceiver.TokoPointDrawerBroadcastReceiver;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.module.ServiceApiModule;

import dagger.Component;

/**
 * @author anggaprasetiyo on 04/12/17.
 */
@LoyaltyScope
@Component(modules = ServiceApiModule.class, dependencies = BaseAppComponent.class)
public interface TokoPointBroadcastComponent {
    void inject(TokoPointDrawerBroadcastReceiver tokoPointDrawerBroadcastReceiver);
}
