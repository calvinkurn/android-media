package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.manage.item.di.module.ProductDraftModule;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;
import com.tokopedia.product.manage.item.view.fragment.ProductDraftEditFragment;

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
