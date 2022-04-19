package com.tokopedia.minicart.common.widget.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedViewModel
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MiniCartWidgetViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(MiniCartViewModel::class)
    internal abstract fun miniCartWidgetViewModel(viewModel: MiniCartViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(MiniCartSimplifiedViewModel::class)
    internal abstract fun miniCartSimplifiedWidgetViewModel(viewModel: MiniCartSimplifiedViewModel): ViewModel

}