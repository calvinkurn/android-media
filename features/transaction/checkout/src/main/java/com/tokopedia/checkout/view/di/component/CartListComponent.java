package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;
import com.tokopedia.checkout.view.feature.cartlist.CartFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = CartListModule.class, dependencies = CartComponent.class)
public interface CartListComponent {

    void inject(CartFragment cartFragment);

}
