package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.common.di.component.ProductComponent;
import com.tokopedia.product.edit.di.module.ProductAddModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = ProductComponent.class)
public interface ProductAddComponent {
//    void inject(ProductAddFragment productAddFragment);
}
