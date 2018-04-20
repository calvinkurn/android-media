package com.tokopedia.posapp.product.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.product.productdetail.view.fragment.ProductDetailFragment;

import dagger.Component;

/**
 * Created by okasurya on 8/10/17.
 */

@ProductScope
@Component(
        modules = {ProductModule.class, CartModule.class},
        dependencies = BaseAppComponent.class
)
public interface ProductComponent {
    void inject(ProductDetailFragment fragment);
}
