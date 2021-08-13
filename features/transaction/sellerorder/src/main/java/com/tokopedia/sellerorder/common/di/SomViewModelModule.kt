package com.tokopedia.sellerorder.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by fwidjaja on 2019-08-28.
 */

@Module
abstract class SomViewModelModule {

    @SomScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}