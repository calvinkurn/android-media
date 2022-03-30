package com.tokopedia.shopdiscount.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.di.module.ShopDiscountModule
import com.tokopedia.shopdiscount.di.module.ShopDiscountViewModelModule
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.shopdiscount.manage.presentation.ProductManageActivity
import com.tokopedia.shopdiscount.manage.presentation.ProductManageFragment
import dagger.Component

@ShopDiscountComponentScope
@Component(
    modules = [
        ShopDiscountModule::class,
        ShopDiscountViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ShopDiscountComponent {
    fun inject(activity : ProductManageActivity)
    fun inject(bottomSheet: DiscountBulkApplyBottomSheet)
    fun inject(fragment: ProductManageFragment)
}