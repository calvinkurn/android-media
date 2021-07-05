package com.tokopedia.product.addedit.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.addedit.category.presentation.viewmodel.AddEditProductCategoryViewModel
import com.tokopedia.product.addedit.detail.presentation.viewmodel.AddEditProductDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditProductCategoryViewModelModule {

    @AddEditProductCategoryScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddEditProductCategoryViewModel::class)
    abstract fun addEditProductDetailViewModel(viewModel: AddEditProductCategoryViewModel): ViewModel
}