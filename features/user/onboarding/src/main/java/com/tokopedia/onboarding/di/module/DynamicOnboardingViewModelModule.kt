package com.tokopedia.onboarding.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.onboarding.di.OnboardingScope
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DynamicOnboardingViewModelModule {

    @OnboardingScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @OnboardingScope
    @Binds
    @IntoMap
    @ViewModelKey(DynamicOnboardingViewModel::class)
    internal abstract fun dynamicOnbaordingViewModel(viewModel: DynamicOnboardingViewModel): ViewModel
}