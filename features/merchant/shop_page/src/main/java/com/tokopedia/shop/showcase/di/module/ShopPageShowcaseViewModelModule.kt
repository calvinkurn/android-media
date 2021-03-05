package com.tokopedia.shop.showcase.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.showcase.di.scope.ShopPageShowcaseScope
import com.tokopedia.shop.showcase.presentation.viewmodel.ShopPageShowcaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Rafli Syam on 05/03/2021
 */
@Module
abstract class ShopPageShowcaseViewModelModule {

    @ShopPageShowcaseScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopPageShowcaseViewModel::class)
    internal abstract fun shopPageShowcaseViewModel(viewModel: ShopPageShowcaseViewModel): ViewModel

}