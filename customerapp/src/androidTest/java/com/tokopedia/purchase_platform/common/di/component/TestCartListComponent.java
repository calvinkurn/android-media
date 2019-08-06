package com.tokopedia.purchase_platform.common.di.component;

import com.tokopedia.purchase_platform.common.di.module.TestCartListModule;
import com.tokopedia.purchase_platform.common.di.scope.CartListScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = TestCartListModule.class, dependencies = CartComponent.class)
public interface TestCartListComponent extends CartListComponent {
}
