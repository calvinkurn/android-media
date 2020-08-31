package com.tokopedia.thankyou_native.recommendationdigital.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.thankyou_native.recommendationdigital.presentation.viewmodel.DigitalRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DigitalViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(DigitalRecommendationViewModel::class)
    internal abstract fun provideRecommendationViewModel(viewModelDigital: DigitalRecommendationViewModel): ViewModel
}
