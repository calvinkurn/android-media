package com.tokopedia.minicart.common.widget.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.minicart.common.widget.MiniCartWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MiniCartWidgetViewModelModule {

    @MiniCartWidgetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @MiniCartWidgetScope
    @Binds
    @IntoMap
    @ViewModelKey(MiniCartWidgetViewModel::class)
    internal abstract fun miniCartWidgetViewModel(viewModel: MiniCartWidgetViewModel): ViewModel

}