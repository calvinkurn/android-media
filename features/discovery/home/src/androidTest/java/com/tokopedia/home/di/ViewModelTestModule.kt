package com.tokopedia.home.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelTestModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: TestViewModelFactory): ViewModelProvider.Factory
}
