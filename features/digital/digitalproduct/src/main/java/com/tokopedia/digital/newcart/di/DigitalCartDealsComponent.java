package com.tokopedia.digital.newcart.di;

import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsFragment;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDealsListFragment;

import dagger.Component;

@DigitalDealsScope
@Component(modules = DigitalCartDealsModule.class, dependencies = DigitalCartComponent.class)
public interface DigitalCartDealsComponent {
    DigitalAnalytics digitalAnalytics();

    void inject(DigitalCartDealsFragment digitalCartDealsFragment);

    void inject(DigitalCartDealsListFragment digitalCartDealsListFragment);
}
