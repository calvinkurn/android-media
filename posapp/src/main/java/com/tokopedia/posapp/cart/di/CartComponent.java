package com.tokopedia.posapp.cart.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.base.activity.ReactDrawerPresenterActivity;
import com.tokopedia.posapp.product.productdetail.view.activity.ProductDetailActivity;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;

import dagger.Component;

/**
 * Created by Herdi_WORK on 09.10.17.
 */

@CartScope
@Component(modules = CartModule.class, dependencies = BaseAppComponent.class)
public interface CartComponent {
    void inject(ProductDetailActivity activity);

    void inject(ProductListActivity productListActivity);
}
