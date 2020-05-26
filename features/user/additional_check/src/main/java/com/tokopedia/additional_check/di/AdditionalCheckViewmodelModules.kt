package com.tokopedia.additional_check.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.additional_check.view.BottomSheetCheckViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Yoris Prayogo on 20/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
abstract class AdditionalCheckViewmodelModules{

    @Binds
    @AdditionalCheckScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetCheckViewModel::class)
    internal abstract fun provideBottomSheetCheckViewModel(viewModel: BottomSheetCheckViewModel): ViewModel
}