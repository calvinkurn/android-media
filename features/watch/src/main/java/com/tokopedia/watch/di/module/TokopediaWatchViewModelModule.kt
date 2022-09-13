package com.tokopedia.watch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.watch.TokopediaWatchViewModel
import com.tokopedia.watch.di.scope.TokopediaWatchScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokopediaWatchViewModelModule {

    @Binds
    @TokopediaWatchScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokopediaWatchViewModel::class)
    internal abstract fun tokopediaWatchViewModel(viewModelTokoNow: TokopediaWatchViewModel): ViewModel
}