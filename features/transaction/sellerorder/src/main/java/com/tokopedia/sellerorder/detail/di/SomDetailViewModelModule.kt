package com.tokopedia.sellerorder.detail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-09-30.
 */

@Module
@SomDetailScope
abstract class SomDetailViewModelModule {
    @SomDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomDetailViewModel::class)
    internal abstract fun somListViewModel(viewModel: SomDetailViewModel): ViewModel
}