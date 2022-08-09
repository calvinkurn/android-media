package com.tokopedia.tkpd.flashsale.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tkpd.flashsale.di.scope.TokopediaFlashSaleScope
import com.tokopedia.tkpd.flashsale.presentation.landing.LandingContainerViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class TokopediaFlashSaleViewModelModule {

    @TokopediaFlashSaleScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LandingContainerViewModel::class)
    internal abstract fun provideLandingContainerViewModel(viewModel: LandingContainerViewModel): ViewModel
}