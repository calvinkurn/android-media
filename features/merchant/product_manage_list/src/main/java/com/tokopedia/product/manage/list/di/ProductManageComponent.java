package com.tokopedia.product.manage.list.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.product.manage.list.view.fragment.ProductManageFragment;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSortFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@ProductManageScope
@Component(modules = {ProductManageModule.class, ViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface ProductManageComponent {

    void inject(ProductManageFragment productManageFragment);
    void inject(ProductManageSortFragment productManageSortFragment);
    void inject(ProductManageSellerFragment productManageSellerFragment);

}
