package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.purchase_platform.common.di.module.CartListModule;
import com.tokopedia.purchase_platform.common.di.scope.CartListScope;
import com.tokopedia.purchase_platform.features.cart.view.CartFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = CartListModule.class, dependencies = CartComponent.class)
public interface CartListComponent {

//    void inject(CartFragment cartFragment);

}
