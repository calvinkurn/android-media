package com.tokopedia.product.manage.list.di;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.list.view.fragment.ProductManageFragment;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSortFragment;
import com.tokopedia.seller.SellerModuleRouter;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@ProductManageScope
@Component(modules = ProductManageModule.class, dependencies = ProductComponent.class)
public interface ProductManageComponent {
    void inject(ProductManageFragment productManageFragment);

    void inject(ProductManageSortFragment productManageSortFragment);

    GetShopInfoUseCase getShopInfoUseCase();

    SellerModuleRouter getSellerModuleRouter();
}
