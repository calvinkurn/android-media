package com.tokopedia.product.manage.oldlist.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.product.manage.oldlist.view.fragment.ProductManageFragment;
import com.tokopedia.product.manage.oldlist.view.fragment.ProductManageSellerFragment;
import com.tokopedia.product.manage.oldlist.view.fragment.ProductManageSortFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@OldProductManageScope
@Component(modules = {ProductManageModule.class, ViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface OldProductManageComponent {

    void inject(ProductManageFragment productManageFragment);
    void inject(ProductManageSortFragment productManageSortFragment);
    void inject(ProductManageSellerFragment productManageSellerFragment);

}
