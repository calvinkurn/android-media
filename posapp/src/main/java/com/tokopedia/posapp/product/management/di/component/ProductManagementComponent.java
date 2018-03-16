package com.tokopedia.posapp.product.management.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.product.management.di.module.ProductManagementModule;
import com.tokopedia.posapp.product.management.di.scope.ProductManagementScope;
import com.tokopedia.posapp.product.management.view.fragment.ProductManagementFragment;

import dagger.Component;

/**
 * @author okasurya on 3/14/18.
 */
@ProductManagementScope
@Component(modules = {ProductManagementModule.class}, dependencies = BaseAppComponent.class)
public interface ProductManagementComponent {
    void inject(ProductManagementFragment fragment);
}
