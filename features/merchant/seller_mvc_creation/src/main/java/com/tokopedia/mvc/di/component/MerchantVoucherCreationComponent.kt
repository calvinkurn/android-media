package com.tokopedia.mvc.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mvc.di.module.MerchantVoucherCreationModule
import com.tokopedia.mvc.di.module.MerchantVoucherCreationViewModelModule
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.product.add.AddProductFragment
import com.tokopedia.mvc.presentation.product.list.ProductListActivity
import com.tokopedia.mvc.presentation.product.list.ProductListFragment
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantBottomSheet
import com.tokopedia.mvc.presentation.product.variant.select.SelectVariantBottomSheet
import dagger.Component

@MerchantVoucherCreationScope
@Component(
    modules = [MerchantVoucherCreationModule::class, MerchantVoucherCreationViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface MerchantVoucherCreationComponent {
    fun inject(activity: AddProductActivity)
    fun inject(fragment: AddProductFragment)
    fun inject(fragment: SelectVariantBottomSheet)
    fun inject(fragment: ReviewVariantBottomSheet)
    fun inject(activity: ProductListActivity)
    fun inject(fragment: ProductListFragment)
}
