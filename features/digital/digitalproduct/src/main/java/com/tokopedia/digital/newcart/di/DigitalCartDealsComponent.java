package com.tokopedia.digital.newcart.di;

import com.tokopedia.digital.cart.di.DigitalCartComponent;

import dagger.Component;

@DigitalDealsScope
@Component(modules = DigitalCartDealsModule.class, dependencies = DigitalCartComponent.class)
public interface DigitalCartDealsComponent {
}
