package com.tokopedia.shop_widget.operationalhour.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_widget.operationalhour.view.viewmodel.ShopOperationalHourBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopWidgetViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopOperationalHourBottomSheetViewModel::class)
    internal abstract fun newShopPageViewModel(viewModel: ShopOperationalHourBottomSheetViewModel): ViewModel
}