package com.tokopedia.dilayanitokopedia.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.dilayanitokopedia.ui.home.DtHomeViewModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.DtHomeRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DtHomeViewModel::class)
    internal abstract fun dtHomeViewModel(viewModelDt: DtHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DtHomeRecommendationViewModel::class)
    internal abstract fun dtHomeRecommendation(viewModelDt: DtHomeRecommendationViewModel): ViewModel
}
