package com.tokopedia.checkout.view.di.component;

import com.tokopedia.checkout.view.di.module.TestCartListModule;
import com.tokopedia.checkout.view.di.scope.CartListScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = TestCartListModule.class, dependencies = CartComponent.class)
public interface TestCartListComponent extends CartListComponent {
}
