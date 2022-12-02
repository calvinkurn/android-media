package com.tokopedia.mvc.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.detail.VoucherDetailViewModel
import com.tokopedia.mvc.presentation.download.DownloadVoucherImageViewModel
import com.tokopedia.mvc.presentation.product.add.AddProductViewModel
import com.tokopedia.mvc.presentation.product.list.ProductListViewModel
import com.tokopedia.mvc.presentation.product.variant.review.ReviewVariantViewModel
import com.tokopedia.mvc.presentation.product.variant.select.SelectVariantViewModel
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
    internal abstract fun provideSelectVariantViewModel(viewModel: SelectVariantViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    internal abstract fun provideProductListViewModel(viewModel: ProductListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewVariantViewModel::class)
    internal abstract fun provideReviewVariantViewModel(viewModel: ReviewVariantViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VoucherDetailViewModel::class)
    internal abstract fun provideVoucherDetailViewModel(viewModel: VoucherDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DownloadVoucherImageViewModel::class)
    internal abstract fun provideDownloadVoucherViewModel(viewModel: DownloadVoucherImageViewModel): ViewModel
}
