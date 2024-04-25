package com.tokopedia.navigation.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.navigation.presentation.presenter.MainParentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GlobalNavViewModelModule {

    @Binds
    @GlobalNavScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainParentViewModel::class)
    internal abstract fun getMainParentViewModel(viewModel: MainParentViewModel): ViewModel
}
