package com.tokopedia.shop.settings.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSetOperationalHoursViewModel
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @ShopSettingsScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ShopSettingsScope
    @ViewModelKey(ShopEditBasicInfoViewModel::class)
    internal abstract fun shopEditBasicInfoViewModel(viewModel: ShopEditBasicInfoViewModel): ViewModel


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