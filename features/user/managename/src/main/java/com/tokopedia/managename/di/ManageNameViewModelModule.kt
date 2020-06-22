package com.tokopedia.managename.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.managename.viewmodel.ManageNameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 04/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class ManageNameViewModelModule {

    @Binds
    @ManageNameScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ManageNameScope
    @ViewModelKey(ManageNameViewModel::class)
    abstract fun manageNameViewModel(viewModel: ManageNameViewModel): ViewModel

}
