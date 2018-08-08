package com.tokopedia.product.edit.di.component;

import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.common.di.module.ProductTomeQualifier;
import com.tokopedia.product.edit.di.module.ProductAddModule;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.price.ProductAddNameCategoryFragment;
import com.tokopedia.product.edit.view.fragment.ProductAddFragment;

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
