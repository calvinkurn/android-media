package com.tokopedia.seller.search.feature.initialsearch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.seller.search.feature.initialsearch.di.scope.InitialSearchScope
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityComposeViewModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchActivityViewModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchComposeViewModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchComposeViewModel
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InitialSearchViewModelModule {

    @InitialSearchScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InitialSearchViewModel::class)
    abstract fun initialSearchViewModelModule(initialSearchViewModel: InitialSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SuggestionSearchViewModel::class)
    abstract fun suggestionSearchViewModelModule(suggestionSearchViewModel: SuggestionSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitialSearchActivityViewModel::class)
    abstract fun initialSearchActivityViewModel(initialSearchActivityViewModel: InitialSearchActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitialSearchActivityComposeViewModel::class)
    abstract fun initialSearchActivityComposeViewModel(initialSearchActivityComposeViewModel: InitialSearchActivityComposeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InitialSearchComposeViewModel::class)
    abstract fun initialSearchComposeViewModel(initialSearchComposeViewModel: InitialSearchComposeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SuggestionSearchComposeViewModel::class)
    abstract fun suggestionSearchComposeViewModel(suggestionSearchComposeViewModel: SuggestionSearchComposeViewModel): ViewModel
}
