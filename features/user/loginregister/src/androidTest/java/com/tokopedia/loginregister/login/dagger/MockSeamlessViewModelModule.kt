package com.tokopedia.loginregister.login.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 05/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class MockSeamlessViewModelModule{
    @Binds
    @LoginScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerSeamlessViewModel::class)
    internal abstract fun provideSellerSeamlessViewModel(viewModel: SellerSeamlessViewModel): ViewModel
}