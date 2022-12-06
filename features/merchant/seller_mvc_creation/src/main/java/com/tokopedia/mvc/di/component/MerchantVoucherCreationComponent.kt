package com.tokopedia.mvc.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mvc.di.module.MerchantVoucherCreationModule
import com.tokopedia.mvc.di.module.MerchantVoucherCreationViewModelModule
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.presentation.bottomsheet.displayvoucher.DisplayVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.editperiod.VoucherEditPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.detail.VoucherDetailActivity
import com.tokopedia.mvc.presentation.detail.VoucherDetailFragment
import com.tokopedia.mvc.presentation.download.DownloadVoucherImageBottomSheet
import com.tokopedia.mvc.presentation.list.fragment.MvcListFragment
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
    fun inject(bottomsheet: SelectVariantBottomSheet)

    fun inject(fragment: MvcListFragment)
    fun inject(bottomsheet: FilterVoucherBottomSheet)

    fun inject(activity: VoucherDetailActivity)
    fun inject(fragment: VoucherDetailFragment)
    fun inject(fragment: ReviewVariantBottomSheet)
    fun inject(activity: ProductListActivity)
    fun inject(fragment: ProductListFragment)

    fun inject(fragment: DownloadVoucherImageBottomSheet)

    fun inject(bottomSheet: VoucherEditPeriodBottomSheet)
    fun inject(bottomSheet: DisplayVoucherBottomSheet)
}
