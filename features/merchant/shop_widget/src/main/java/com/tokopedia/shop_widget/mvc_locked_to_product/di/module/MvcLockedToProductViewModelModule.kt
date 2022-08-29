package com.tokopedia.shop_widget.mvc_locked_to_product.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel.MvcLockedToProductViewModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel.MvcLockedToProductSortListBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MvcLockedToProductViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MvcLockedToProductViewModel::class)
    internal abstract fun mvcChooseProductViewModel(viewModel: MvcLockedToProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MvcLockedToProductSortListBottomSheetViewModel::class)
    internal abstract fun mvcLockedToProductSortListBottomSheetViewModel(viewModel: MvcLockedToProductSortListBottomSheetViewModel): ViewModel
}