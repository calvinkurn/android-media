package com.tokopedia.tokofood.feature.search.searchresult.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokofood.feature.search.di.scope.TokoFoodSearchScope
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodQuickPriceRangeViewModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchResultViewModelModule {

    @Binds
    @IntoMap
    @TokoFoodSearchScope
    @ViewModelKey(TokofoodSearchResultPageViewModel::class)
    internal abstract fun provideTokofoodSearchResultPageViewModel(viewModel: TokofoodSearchResultPageViewModel): ViewModel

    @Binds
    @IntoMap
    @TokoFoodSearchScope
    @ViewModelKey(TokofoodQuickPriceRangeViewModel::class)
    internal abstract fun provideTokofoodQuickPriceRangeViewModel(viewModel: TokofoodQuickPriceRangeViewModel): ViewModel

}