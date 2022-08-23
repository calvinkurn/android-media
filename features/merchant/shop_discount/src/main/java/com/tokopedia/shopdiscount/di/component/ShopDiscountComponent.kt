package com.tokopedia.shopdiscount.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.di.module.ShopDiscountModule
import com.tokopedia.shopdiscount.di.module.ShopDiscountViewModelModule
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.shopdiscount.info.presentation.bottomsheet.ShopDiscountSellerInfoBottomSheet
import com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageActivity
import com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageFragment
import com.tokopedia.shopdiscount.manage.presentation.list.DiscountedProductListFragment
import com.tokopedia.shopdiscount.manage_discount.presentation.view.fragment.ShopDiscountManageFragment
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageProductFragment
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageVariantFragment
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountMultiLocEduFragment
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductActivity
import com.tokopedia.shopdiscount.search.presentation.SearchProductFragment
import com.tokopedia.shopdiscount.select.presentation.SelectProductActivity
import com.tokopedia.shopdiscount.select.presentation.SelectProductFragment
import com.tokopedia.shopdiscount.set_period.presentation.bottomsheet.SetPeriodBottomSheet
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
    fun inject(activity : DiscountedProductManageActivity)
    fun inject(bottomSheet: DiscountBulkApplyBottomSheet)
    fun inject(fragment: DiscountedProductManageFragment)
    fun inject(bottomSheet: ShopDiscountProductDetailBottomSheet)
    fun inject(fragment : DiscountedProductListFragment)
    fun inject(activity : SearchProductActivity)
    fun inject(bottomSheet: ShopDiscountSellerInfoBottomSheet)
    fun inject(fragment: SearchProductFragment)
    fun inject(fragment: SelectProductFragment)
    fun inject(fragment: SelectProductActivity)
    fun inject(fragment: ShopDiscountManageFragment)
    fun inject(fragment: ShopDiscountManageProductFragment)
    fun inject(bottomSheet: SetPeriodBottomSheet)
    fun inject(fragment: ShopDiscountMultiLocEduFragment)
    fun inject(fragment: ShopDiscountManageVariantFragment)
}