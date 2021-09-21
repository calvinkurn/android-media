package com.tokopedia.logisticorder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticorder.view.TrackingPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TrackingPageViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TrackingPageViewModel::class)
    internal abstract fun provideTrackingPageViewModel(viewModel: TrackingPageViewModel): ViewModel
}