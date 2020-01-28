package com.tokopedia.product.manage.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.list.view.presenter.ProductManageSortViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ProductManageScope
abstract  class ViewModelModule {

    @ProductManageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductManageSortViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: ProductManageSortViewModel): ViewModel

}