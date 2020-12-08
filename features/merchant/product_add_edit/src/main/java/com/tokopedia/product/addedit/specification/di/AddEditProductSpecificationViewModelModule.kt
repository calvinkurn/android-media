package com.tokopedia.product.addedit.specification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.specification.presentation.viewmodel.AddEditProductSpecificationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@AddEditProductSpecificationScope
abstract class AddEditProductSpecificationViewModelModule {

    @AddEditProductSpecificationScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductSpecificationViewModel::class)
    abstract fun addEditProductSpecificationViewModel(viewModel: AddEditProductSpecificationViewModel): ViewModel
}