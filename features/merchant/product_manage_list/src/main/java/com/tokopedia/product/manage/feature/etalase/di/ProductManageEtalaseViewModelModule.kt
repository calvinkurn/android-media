package com.tokopedia.product.manage.feature.etalase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.manage.feature.cashback.presentation.viewmodel.ProductManageSetCashbackViewModel
import com.tokopedia.product.manage.feature.etalase.view.viewmodel.EtalasePickerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ProductManageEtalaseScope
abstract class ProductManageEtalaseViewModelModule {

    @ProductManageEtalaseScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EtalasePickerViewModel::class)
    internal abstract fun productManageSetCashbackViewModel(viewModel: ProductManageSetCashbackViewModel): ViewModel

}