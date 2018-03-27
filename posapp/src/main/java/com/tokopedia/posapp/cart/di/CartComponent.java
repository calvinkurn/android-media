package com.tokopedia.posapp.cart.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.base.activity.ReactDrawerPresenterActivity;
import com.tokopedia.posapp.product.productdetail.view.activity.ProductDetailActivity;

import dagger.Component;

/**
 * Created by Herdi_WORK on 09.10.17.
 */

@CartScope
@Component(modules = CartModule.class, dependencies = AppComponent.class)
public interface CartComponent {
    void inject(ProductDetailActivity activity);
    void inject(ReactDrawerPresenterActivity reactDrawerPresenterActivity);
}
