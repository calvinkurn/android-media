package com.tokopedia.product.addedit.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditProductDetailViewModelModule {

    @AddEditProductDetailScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductDetailViewModel::class)
    abstract fun addEditProductDetailViewModel(viewModel: AddEditProductDetailViewModel): ViewModel
}