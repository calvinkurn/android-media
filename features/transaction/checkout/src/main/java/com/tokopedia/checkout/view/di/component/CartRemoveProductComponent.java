package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.checkout.view.feature.removecartitem.RemoveCartItemFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartRemoveProductScope
@Component(modules = CartRemoveProductModule.class, dependencies = CartComponent.class)
public interface CartRemoveProductComponent {
    void inject(RemoveCartItemFragment removeCartItemFragment);
}
