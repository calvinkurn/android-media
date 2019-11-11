package com.tokopedia.product.manage.list.di;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSellerFragment;

import dagger.Component;

/**
 * Created by hendry on 6/21/2017.
 */

@ProductManageScope
@Component(modules = ProductDraftListCountModule.class, dependencies = ProductComponent.class)
public interface ProductDraftListCountComponent {
    void inject(ProductManageSellerFragment productManageSellerFragment);
}
