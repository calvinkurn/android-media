package com.tokopedia.mvc.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mvc.di.module.ImageGeneratorModule
import com.tokopedia.mvc.di.module.CommonViewModelModule
import com.tokopedia.mvc.di.module.MerchantVoucherCreationModule
import com.tokopedia.mvc.di.module.MerchantVoucherCreationViewModelModule
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.changequota.ChangeQuotaBottomSheet
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeActivity
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeFragment
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationFragment
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
import com.tokopedia.mvc.presentation.summary.fragment.SummaryFragment
import dagger.Component

@MerchantVoucherCreationScope
@Component(
    modules = [MerchantVoucherCreationModule::class, MerchantVoucherCreationViewModelModule::class,
        ImageGeneratorModule::class,
        CommonViewModelModule::class],
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

    fun inject(fragment: SummaryFragment)

    fun inject(fragment: DownloadVoucherImageBottomSheet)

    fun inject(activity: VoucherTypeActivity)
    fun inject(fragment: VoucherTypeFragment)
    fun inject(activity: VoucherInformationActivity)
    fun inject(fragment: VoucherInformationFragment)

    fun inject(bottomSheet: ChangeQuotaBottomSheet)
}
