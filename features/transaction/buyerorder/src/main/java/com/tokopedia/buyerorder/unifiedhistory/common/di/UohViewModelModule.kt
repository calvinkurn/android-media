package com.tokopedia.buyerorder.unifiedhistory.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by fwidjaja on 04/07/20.
 */

@Module
@UohScope
abstract class UohViewModelModule {

    @UohScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}