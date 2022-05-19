package com.tokopedia.minicart.common.widget.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.common.general.MiniCartGeneralViewModel
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MiniCartGeneralWidgetViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(MiniCartGeneralViewModel::class)
    internal abstract fun miniCartGeneralWidgetViewModel(viewModel: MiniCartGeneralViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(MiniCartSimplifiedViewModel::class)
    internal abstract fun miniCartSimplifiedWidgetViewModel(viewModel: MiniCartSimplifiedViewModel): ViewModel
}