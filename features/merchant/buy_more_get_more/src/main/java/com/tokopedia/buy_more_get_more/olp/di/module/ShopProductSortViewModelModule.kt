package com.tokopedia.buy_more_get_more.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buy_more_get_more.di.scope.ShopProductSortScope
import com.tokopedia.buy_more_get_more.sort.viewmodel.ShopProductSortViewModel
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
