package com.tokopedia.onboarding.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.onboarding.view.viewmodel.DynamicOnbaordingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@OnboardingScope
@Module
abstract class DynamicOnboardingViewModelModule {

    @OnboardingScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @OnboardingScope
    @Binds
    @IntoMap
    @ViewModelKey(DynamicOnbaordingViewModel::class)
    internal abstract fun dynamicOnbaordingViewModel(viewModel: DynamicOnbaordingViewModel): ViewModel
}