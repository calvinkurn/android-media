package com.tokopedia.digital.digital_recommendation.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.digital_recommendation.presentation.viewmodel.DigitalRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 23/09/2021
 */
@Module
abstract class DigitalRecommendationViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DigitalRecommendationViewModel::class)
    abstract fun digitalRecommendationViewModel(viewModel: DigitalRecommendationViewModel): ViewModel
}