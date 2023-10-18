package com.tokopedia.shop.sort.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.sort.di.scope.ShopProductSortScope
import com.tokopedia.shop.sort.view.viewmodel.ShopProductSortViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopProductSortViewModelModule {

    @ShopProductSortScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopProductSortViewModel::class)
    internal abstract fun shopProductSortViewModel(viewModel: ShopProductSortViewModel): ViewModel

}
