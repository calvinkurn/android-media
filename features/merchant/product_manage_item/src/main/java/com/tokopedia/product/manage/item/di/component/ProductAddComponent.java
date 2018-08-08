package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.manage.item.di.module.ProductAddModule;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;
import com.tokopedia.product.manage.item.price.ProductAddNameCategoryFragment;
import com.tokopedia.product.manage.item.view.fragment.ProductAddFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = ProductComponent.class)
public interface ProductAddComponent {
//    void inject(ProductAddNameCategoryFragment productAddFragment);
    void inject(ProductAddFragment productAddFragment);
}
