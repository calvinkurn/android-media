package com.tokopedia.manageaddress.di.manageaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.manageaddress.di.manageaddress.ManageAddressScope
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ManageAddressScope
@Module
abstract class ManageAddressViewModelModule {

    @ManageAddressScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ManageAddressViewModel::class)
    internal abstract fun providesManageAddressViewModel(viewModel: ManageAddressViewModel): ViewModel
}