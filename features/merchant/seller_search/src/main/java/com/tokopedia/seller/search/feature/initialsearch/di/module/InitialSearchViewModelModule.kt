package com.tokopedia.seller.search.feature.initialsearch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.seller.search.feature.initialsearch.di.scope.InitialSearchScope
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@InitialSearchScope
abstract class InitialSearchViewModelModule {
    
    @InitialSearchScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    
    @Binds
    @IntoMap
    @ViewModelKey(InitialSearchViewModel::class)
    abstract fun initialSearchViewModelModule(initialSearchViewModel: InitialSearchViewModel): ViewModel
}