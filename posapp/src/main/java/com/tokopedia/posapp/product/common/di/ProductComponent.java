package com.tokopedia.posapp.product.common.di;

import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.di.component.PosAppComponent;
import com.tokopedia.posapp.product.productdetail.view.fragment.ProductDetailFragment;

import dagger.Component;

/**
 * Created by okasurya on 8/10/17.
 */

@ProductScope
@Component(
        modules = {ProductModule.class, CartModule.class},
        dependencies = PosAppComponent.class
)
public interface ProductComponent {
    void inject(ProductDetailFragment fragment);
}
