package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.edit.di.module.ProductDraftModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.view.fragment.ProductDraftEditFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sebastianuskh on 4/26/17.
 */

@ProductAddScope
@Component(modules = ProductDraftModule.class, dependencies = ProductComponent.class)
public interface ProductDraftComponent extends ProductAddComponent {
    void inject(ProductDraftEditFragment fragment);
}
