package com.tokopedia.product.edit.di.component;

import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.edit.di.module.ProductEditModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.view.fragment.ProductEditFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sebastianuskh on 4/21/17.
 */
@ProductAddScope
@Component(modules = ProductEditModule.class, dependencies = ProductComponent.class)
public interface ProductEditComponent extends ProductAddComponent {
    void inject(ProductEditFragment fragment);
}
