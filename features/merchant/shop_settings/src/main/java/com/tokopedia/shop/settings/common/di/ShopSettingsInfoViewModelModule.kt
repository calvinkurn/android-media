package com.tokopedia.shop.settings.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopSettingsInfoViewModelModule {

    @ShopSettingsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopSettingsInfoViewModel::class)
    internal abstract fun shopSettingsInfoViewModel(viewModel: ShopSettingsInfoViewModel): ViewModel

}