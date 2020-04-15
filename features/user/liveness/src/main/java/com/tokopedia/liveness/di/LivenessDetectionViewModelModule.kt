package com.tokopedia.liveness.di

import com.tokopedia.liveness.view.viewmodel.LivenessDetectionViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@LivenessDetectionScope
abstract class LivenessDetectionViewModelModule {

    @Binds
    @LivenessDetectionScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LivenessDetectionViewModel::class)
    abstract fun livenessDetectionViewModel(viewModel: LivenessDetectionViewModel): ViewModel
}