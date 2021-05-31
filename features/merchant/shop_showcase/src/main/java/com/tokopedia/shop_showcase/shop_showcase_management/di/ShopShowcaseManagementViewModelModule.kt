package com.tokopedia.shop_showcase.shop_showcase_management.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopShowcaseManagementViewModelModule {

    @ShopShowcaseManagementScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopShowcaseListViewModel::class)
    internal abstract fun shopShowcaseViewModel(viewModel: ShopShowcaseListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopShowcasePickerViewModel::class)
    internal abstract fun shopShowcasePickerViewModel(viewModel: ShopShowcasePickerViewModel): ViewModel

}