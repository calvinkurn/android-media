package com.tokopedia.tokofood.feature.search.searchresult.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.searchresult.di.scope.SearchResultScope
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchResultViewModelModule {

    @Binds
    @TokoFoodScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TokoFoodScope
    @ViewModelKey(TokofoodSearchResultPageViewModel::class)
    internal abstract fun provideTokofoodSearchResultPageViewModel(viewModel: TokofoodSearchResultPageViewModel): ViewModel

}