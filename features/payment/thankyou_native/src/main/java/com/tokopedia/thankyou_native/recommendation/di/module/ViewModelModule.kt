package com.tokopedia.thankyou_native.recommendation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.thankyou_native.recommendation.presentation.viewmodel.MarketPlaceRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MarketPlaceRecommendationViewModel::class)
    internal abstract fun provideRecommendationViewModel(viewModelDigital: MarketPlaceRecommendationViewModel): ViewModel
}
