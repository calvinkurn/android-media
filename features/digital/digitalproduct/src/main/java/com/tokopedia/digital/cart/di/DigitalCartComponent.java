package com.tokopedia.digital.cart.di;

import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.cart.presentation.fragment.CartDigitalFragment;

import dagger.Component;

/**
 * Created by Rizky on 28/08/18.
 */
@DigitalCartScope
@Component(dependencies = DigitalComponent.class, modules = DigitalCartModule.class)
public interface DigitalCartComponent {

    void inject(CartDigitalFragment cartDigitalFragment);

}