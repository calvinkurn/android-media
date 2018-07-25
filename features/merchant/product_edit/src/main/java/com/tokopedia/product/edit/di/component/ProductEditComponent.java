package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.common.di.component.ProductComponent;
import com.tokopedia.product.edit.di.module.ProductEditModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;

import dagger.Component;

/**
 * @author sebastianuskh on 4/21/17.
 */
@ProductAddScope
@Component(modules = ProductEditModule.class, dependencies = ProductComponent.class)
public interface ProductEditComponent extends ProductDraftComponent {
//    void inject(ProductEditFragment fragment);
//
//    void inject(ProductDuplicateFragment fragment);
}
