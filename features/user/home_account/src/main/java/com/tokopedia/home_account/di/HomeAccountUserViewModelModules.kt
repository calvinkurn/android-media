package com.tokopedia.home_account.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home_account.view.HomeAccountUserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 20/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class HomeAccountUserViewModelModules{

    @Binds
    @HomeAccountUserScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeAccountUserViewModel::class)
    internal abstract fun provideHomeAccountUserViewModel(viewModel: HomeAccountUserViewModel): ViewModel
}