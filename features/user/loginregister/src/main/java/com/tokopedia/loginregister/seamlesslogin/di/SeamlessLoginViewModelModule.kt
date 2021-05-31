package com.tokopedia.loginregister.seamlesslogin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.seamlesslogin.SeamlessLoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 13/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class SeamlessLoginViewModelModule{

    @Binds
    @SeamlessLoginScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SeamlessLoginViewModel::class)
    internal abstract fun seamlessLoginViewModel(viewModel: SeamlessLoginViewModel): ViewModel
}