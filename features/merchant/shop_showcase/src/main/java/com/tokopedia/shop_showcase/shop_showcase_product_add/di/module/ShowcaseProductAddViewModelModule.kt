package com.tokopedia.shop_showcase.shop_showcase_product_add.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.scope.ShowcaseProductAddScope
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewmodel.ShowcaseProductAddViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by Rafli Syam on 2020-03-09
 */

@Module
abstract class ShowcaseProductAddViewModelModule {

    @Binds
    @ShowcaseProductAddScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @ShowcaseProductAddScope
    @IntoMap
    @ViewModelKey(ShowcaseProductAddViewModel::class)
    abstract fun showcaseProductAddViewModel(showcaseProductAddViewModel: ShowcaseProductAddViewModel): ViewModel

}