package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.di.module.ProductDraftModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;

import dagger.Component;

/**
 * @author sebastianuskh on 4/26/17.
 */

@ProductAddScope
@Component(modules = ProductDraftModule.class, dependencies = ProductComponent.class)
public interface ProductDraftComponent extends ProductAddComponent {
//    void inject(ProductDraftAddFragment fragment);
//    void inject(ProductDraftEditFragment fragment);
}
