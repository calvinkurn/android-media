package com.tokopedia.buyerorder.unifiedhistory.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 04/07/20.
 */

@Module
abstract class UohListViewModelModule {

    @UohListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @UohListScope
    @Binds
    @IntoMap
    @ViewModelKey(UohListViewModel::class)
    internal abstract fun uohListViewModel(viewModel: UohListViewModel): ViewModel
}