package com.tokopedia.manageaddress.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressViewModel
import com.tokopedia.manageaddress.ui.shoplocation.ShopLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManageAddressViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ManageAddressViewModel::class)
    internal abstract fun providesManageAddressViewModel(viewModel: ManageAddressViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ShopLocationViewModel::class)
    internal abstract fun providesShopLocationViewModel(viewModel: ShopLocationViewModel): ViewModel
}