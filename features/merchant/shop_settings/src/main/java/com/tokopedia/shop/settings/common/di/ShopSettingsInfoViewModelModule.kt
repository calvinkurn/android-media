package com.tokopedia.shop.settings.common.di

import dagger.Module

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
