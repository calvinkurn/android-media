package com.tokopedia.find_native.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.find_native.di.scope.FindNavScope
import com.tokopedia.find_native.viewmodel.FindNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FindNavViewModelModule {

    @Binds
    @FindNavScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FindNavScope
    @ViewModelKey(FindNavViewModel::class)
    internal abstract fun findNavViewModel(viewModel: FindNavViewModel): ViewModel
}