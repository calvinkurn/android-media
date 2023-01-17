package com.tokopedia.dilayanitokopedia.home.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.dilayanitokopedia.home.di.scope.HomeScope
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeRecommendationForYouViewModel
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @HomeScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DtHomeViewModel::class)
    internal abstract fun dtHomeViewModel(viewModelDt: DtHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DtHomeRecommendationForYouViewModel::class)
    internal abstract fun dtHomeRecommendation(viewModelDt: DtHomeRecommendationForYouViewModel): ViewModel
}
