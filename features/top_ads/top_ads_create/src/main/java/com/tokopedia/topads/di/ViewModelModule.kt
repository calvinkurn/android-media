package com.tokopedia.topads.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.view.model.CreateGroupAdsViewModel
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.topads.view.model.ProductAdsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 08,November,2019
 */
@CreateAdsScope
@Module
abstract class ViewModelModule {

    @CreateAdsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CreateGroupAdsViewModel::class)
    internal abstract fun provideCreateGroupAdsViewModel(viewModel: CreateGroupAdsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductAdsListViewModel::class)
    internal abstract fun provideProductListViewModel(viewModel: ProductAdsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KeywordAdsViewModel::class)
    internal abstract fun provideKeywordAdsViewModel(viewModel: KeywordAdsViewModel): ViewModel
}