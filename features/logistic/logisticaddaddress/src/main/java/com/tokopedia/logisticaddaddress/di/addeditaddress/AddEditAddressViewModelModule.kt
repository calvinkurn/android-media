package com.tokopedia.logisticaddaddress.di.addeditaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.features.addeditaddress.addressform.AddressFormViewModel
import com.tokopedia.logisticaddaddress.features.addeditaddress.search.SearchPageViewModel
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditAddressViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(SearchPageViewModel::class)
    internal abstract fun providesSearchPageViewModel(viewModel: SearchPageViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(PinpointViewModel::class)
    internal abstract fun providesPinpointViewModel(viewModel: PinpointViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(AddressFormViewModel::class)
    internal abstract fun providesAddressFormViewModel(viewModel: AddressFormViewModel): ViewModel
}
