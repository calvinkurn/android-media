package com.tokopedia.checkout.view.feature.emptycart.di;

import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 17/09/18.
 */

@EmptyCartScope
@Component(modules = EmptyCartModule.class, dependencies = CartComponent.class)
public interface EmptyCartComponent {
    void inject(EmptyCartFragment emptyCartFragment);
}
