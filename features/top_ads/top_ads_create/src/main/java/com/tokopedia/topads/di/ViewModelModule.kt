package com.tokopedia.topads.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.view.model.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Author errysuprayogi on 08,November,2019
 */
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

    @Binds
    @IntoMap
    @ViewModelKey(BudgetingAdsViewModel::class)
    internal abstract fun provideBudgetingAdsViewModel(viewModel: BudgetingAdsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SummaryViewModel::class)
    internal abstract fun provideSummaryViewModel(viewModel: SummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdChooserViewModel::class)
    internal abstract fun provideAdChooserViewModel(viewModel: AdChooserViewModel): ViewModel
}
