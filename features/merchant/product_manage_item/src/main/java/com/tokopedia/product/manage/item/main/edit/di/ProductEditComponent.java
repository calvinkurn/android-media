package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.manage.item.di.module.ProductEditModule;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;
import com.tokopedia.product.manage.item.view.fragment.ProductEditFragment;

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
