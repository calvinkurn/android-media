package com.tokopedia.logisticaddaddress.di.addnewaddressrevamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageViewModel
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddNewAddressRevampViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(SearchPageViewModel::class)
    internal abstract fun providesShopLocationViewModel(viewModel: SearchPageViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(PinpointNewPageViewModel::class)
    internal abstract fun providesPinpointNewPageViewModel(viewModel: PinpointNewPageViewModel): ViewModel
}