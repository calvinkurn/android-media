package com.tokopedia.tokopedianow.annotation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.annotation.di.scope.AllAnnotationScope
import com.tokopedia.tokopedianow.annotation.presentation.viewmodel.TokoNowAllAnnotationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AllAnnotationViewModelModule {

    @Binds
    @AllAnnotationScope
    internal abstract fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowAllAnnotationViewModel::class)
    internal abstract fun viewModel(viewModel: TokoNowAllAnnotationViewModel): ViewModel
}
