package com.tokopedia.shop_showcase.shop_showcase_add.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_showcase.shop_showcase_add.di.scope.ShopShowcaseAddScope
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel.ShopShowcaseAddViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by Rafli Syam on 2020-03-10
 */

@Module
abstract class ShopShowcaseAddViewModelModule {

    @Binds
    @ShopShowcaseAddScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ShopShowcaseAddScope
    @IntoMap
    @ViewModelKey(ShopShowcaseAddViewModel::class)
    abstract fun showcaseProductAddViewModel(shopShowcaseAddViewModel: ShopShowcaseAddViewModel): ViewModel

}