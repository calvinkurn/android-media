package com.tokopedia.shop.settings.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.settings.setting.view.viewmodel.ShopPageSettingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class ShopSettingViewModelModule {

    @Binds
    @ShopSettingsScope
    @Named(Constant.SHOP_SETTING_VIEW_MODEL_FACTORY)
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ShopSettingsScope
    @ViewModelKey(ShopPageSettingViewModel::class)
    internal abstract fun shopPageSettingViewModel(viewModel: ShopPageSettingViewModel): ViewModel
}
