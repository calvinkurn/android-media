package com.tokopedia.product.addedit.draft.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.draft.presentation.viewmodel.AddEditProductDraftViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditProductDraftViewModelModule {

    @AddEditProductDraftScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductDraftViewModel::class)
    abstract fun addEditProductDraftViewModel(viewModel: AddEditProductDraftViewModel): ViewModel
}