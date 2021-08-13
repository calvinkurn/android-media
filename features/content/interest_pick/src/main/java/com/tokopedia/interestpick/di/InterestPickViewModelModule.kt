package com.tokopedia.interestpick.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.interestpick.view.viewmodel.InterestPickViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InterestPickViewModelModule {

    @Binds
    @InterestPickScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @InterestPickScope
    @ViewModelKey(InterestPickViewModel::class)
    internal abstract fun interestPickViewModel(viewModel: InterestPickViewModel): ViewModel
}