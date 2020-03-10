package com.tokopedia.product.manage.feature.cashback.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import dagger.Binds
import dagger.multibindings.IntoMap

abstract class ProductManageSetCashbackViewModelModule {

    @ProductManageSetCashbackScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(ProductManageSetCashbackViewModel::class)
//    internal abstract fun productManageFilterViewModel(viewModel: ProductManageSetCashbackViewModel): ViewModel

}