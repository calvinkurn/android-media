package com.tokopedia.product.addedit.description.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditProductDescriptionViewModelModule {

    @AddEditProductDescriptionScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductDescriptionViewModel::class)
    abstract fun addEditProductDescriptionViewModel(viewmodel: AddEditProductDescriptionViewModel): ViewModel

}