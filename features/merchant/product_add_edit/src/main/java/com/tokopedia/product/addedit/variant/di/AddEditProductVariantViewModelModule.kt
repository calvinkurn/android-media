package com.tokopedia.product.addedit.variant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantDetailViewModel
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditProductVariantViewModelModule {

    @AddEditProductVariantScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductVariantViewModel::class)
    abstract fun addEditProductVariantViewModel(viewModel: AddEditProductVariantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductVariantDetailViewModel::class)
    abstract fun addEditProductVariantDetailViewModel(viewModel: AddEditProductVariantViewModel): ViewModel

}