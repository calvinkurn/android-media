package com.tokopedia.home.beranda.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.presentation.view.viewmodel.TabBusinessViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@HomeScope
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TabBusinessViewModel::class)
    internal abstract fun tabBusinessViewModel(viewModel: TabBusinessViewModel): ViewModel

}