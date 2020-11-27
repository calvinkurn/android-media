package com.tokopedia.loginregister.external_register.base.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.external_register.ovo.viewmodel.OvoAddNameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 19/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class ExternalRegisterUseCaseViewModelModules {
    @Binds
    @ExternalRegisterScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(BaseAddNameViewModel::class)
//    internal abstract fun provideBaseAddNameViewModel(viewModel: BaseAddNameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OvoAddNameViewModel::class)
    internal abstract fun provideOvoAddNameViewModel(viewModel: OvoAddNameViewModel): ViewModel

}