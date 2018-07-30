package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.di.module.ProductAddModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.price.ProductAddNameCategoryFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = ProductComponent.class)
public interface ProductAddComponent {
    void inject(ProductAddNameCategoryFragment productAddFragment);
}
