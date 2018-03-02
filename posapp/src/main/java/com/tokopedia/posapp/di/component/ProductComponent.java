package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.CartModule;
import com.tokopedia.posapp.di.module.ProductModule;
import com.tokopedia.posapp.di.scope.ProductScope;
import com.tokopedia.posapp.product.productdetail.view.fragment.ProductDetailFragment;

import dagger.Component;

/**
 * Created by okasurya on 8/10/17.
 */

@ProductScope
@Component(
        modules = {ProductModule.class, CartModule.class},
        dependencies = AppComponent.class
)
public interface ProductComponent {
    void inject(ProductDetailFragment fragment);
}
