package com.tokopedia.product.addedit.shipment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.shipment.presentation.viewmodel.AddEditProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@AddEditProductShipmentScope
abstract class AddEditProductShipmentModelModule {

    @AddEditProductShipmentScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductViewModel::class)
    abstract fun addEditProductViewModel(viewModel: AddEditProductViewModel): ViewModel

}