package com.tokopedia.top_ads_on_boarding.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.top_ads_on_boarding.view.viewmodel.TopAdsOnBoardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OnboardingViewModelModule {

    @TopAdsOnBoardingScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsOnBoardingViewModel::class)
    internal abstract fun provideTopAdsOnBoardingViewModel(viewModel: TopAdsOnBoardingViewModel) : ViewModel
}
