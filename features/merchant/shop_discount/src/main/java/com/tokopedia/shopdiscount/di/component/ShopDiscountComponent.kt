package com.tokopedia.shopdiscount.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.di.module.ShopDiscountModule
import com.tokopedia.shopdiscount.di.module.ShopDiscountViewModelModule
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.shopdiscount.info.presentation.bottomsheet.ShopDiscountSellerInfoBottomSheet
import com.tokopedia.shopdiscount.manage.presentation.container.ProductManageActivity
import com.tokopedia.shopdiscount.manage.presentation.container.ProductManageFragment
import com.tokopedia.shopdiscount.manage.presentation.list.ProductListFragment
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductActivity
import com.tokopedia.shopdiscount.search.presentation.SearchProductFragment
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
    fun inject(bottomSheet: ShopDiscountProductDetailBottomSheet)
    fun inject(fragment : ProductListFragment)
    fun inject(activity : SearchProductActivity)
    fun inject(bottomSheet: ShopDiscountSellerInfoBottomSheet)
    fun inject(fragment: SearchProductFragment)
}