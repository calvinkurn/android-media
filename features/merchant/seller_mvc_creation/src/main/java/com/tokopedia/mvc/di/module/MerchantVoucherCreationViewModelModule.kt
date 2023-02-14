package com.tokopedia.mvc.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.DisplayVoucherViewModel
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.FilterVoucherViewModel
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.MoreMenuViewModel
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.VoucherEditCalendarViewModel
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.VoucherEditPeriodViewModel
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeViewModel
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationViewModel
import com.tokopedia.mvc.presentation.creation.step3.VoucherSettingViewModel
import com.tokopedia.mvc.presentation.detail.VoucherDetailViewModel
import com.tokopedia.mvc.presentation.download.DownloadVoucherImageViewModel
import com.tokopedia.mvc.presentation.intro.viewmodel.MvcIntroViewModel
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.product.add.AddProductViewModel
import com.tokopedia.mvc.presentation.product.list.ProductListViewModel
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantViewModel
import com.tokopedia.mvc.presentation.product.variant.select.SelectVariantViewModel
import com.tokopedia.mvc.presentation.quota.viewmodel.QuotaInfoViewModel
import com.tokopedia.mvc.presentation.summary.viewmodel.SummaryViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class MerchantVoucherCreationViewModelModule {

    @MerchantVoucherCreationScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddProductViewModel::class)
    internal abstract fun provideAddProductViewModel(viewModel: AddProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MvcListViewModel::class)
    internal abstract fun provideMvcListViewModel(viewModel: MvcListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectVariantViewModel::class)
    internal abstract fun provideSelectVariantViewModel(viewModel: SelectVariantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterVoucherViewModel::class)
    internal abstract fun provideFilterVoucherViewModel(viewModel: FilterVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    internal abstract fun provideProductListViewModel(viewModel: ProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewVariantViewModel::class)
    internal abstract fun provideReviewVariantViewModel(viewModel: ReviewVariantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherDetailViewModel::class)
    internal abstract fun provideVoucherDetailViewModel(viewModel: VoucherDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherEditPeriodViewModel::class)
    internal abstract fun provideVoucherEditPeriodViewModel(viewModel: VoucherEditPeriodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DisplayVoucherViewModel::class)
    internal abstract fun provideDisplayVoucherViewModel(viewModel: DisplayVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DownloadVoucherImageViewModel::class)
    internal abstract fun provideDownloadVoucherViewModel(viewModel: DownloadVoucherImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreMenuViewModel::class)
    internal abstract fun provideMoreMenuViewModel(viewModel: MoreMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SummaryViewModel::class)
    internal abstract fun provideSummaryViewModel(viewModel: SummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherTypeViewModel::class)
    internal abstract fun provideVoucherCreationStepOneViewModel(viewModel: VoucherTypeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherInformationViewModel::class)
    internal abstract fun provideVoucherCreationStepTwoViewModel(viewModel: VoucherInformationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherSettingViewModel::class)
    internal abstract fun provideVoucherCreationStepThreeViewModel(viewModel: VoucherSettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuotaInfoViewModel::class)
    internal abstract fun provideQuotaInfoViewModel(viewModel: QuotaInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherEditCalendarViewModel::class)
    internal abstract fun provideVoucherEditCalendarViewModel(viewModel: VoucherEditCalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MvcIntroViewModel::class)
    internal abstract fun provideMvcIntroViewModel(viewModel: MvcIntroViewModel): ViewModel
}
