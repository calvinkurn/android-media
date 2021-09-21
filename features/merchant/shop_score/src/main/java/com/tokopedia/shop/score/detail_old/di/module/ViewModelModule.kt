package com.tokopedia.shop.score.detail_old.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.score.detail_old.di.scope.ShopScoreScope
import com.tokopedia.shop.score.detail_old.view.viewmodel.ShopScoreDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @ShopScoreScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopScoreDetailViewModel::class)
    internal abstract fun shopScoreDetailViewModel(viewModel: ShopScoreDetailViewModel): ViewModel
}