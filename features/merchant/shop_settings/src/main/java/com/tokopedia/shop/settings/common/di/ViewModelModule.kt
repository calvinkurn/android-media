package com.tokopedia.shop.settings.common.di

import dagger.Module

@Module
abstract class ViewModelModule

    @Binds
    @IntoMap
    @ShopSettingsScope
    @ViewModelKey(ShopSettingsOperationalHoursViewModel::class)
    internal abstract fun shopSettingsOperationalHoursViewModel(viewModel: ShopSettingsOperationalHoursViewModel): ViewModel

    @Binds
    @IntoMap
    @ShopSettingsScope
    @ViewModelKey(ShopSetOperationalHoursViewModel::class)
    internal abstract fun shopSetOperationalHourViewModel(viewModel: ShopSetOperationalHoursViewModel): ViewModel
}
