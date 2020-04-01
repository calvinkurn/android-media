package com.tokopedia.sellerorder.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 2019-08-28.
 */

@Module
@SomListScope
abstract class SomListViewModelModule {

    @SomListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomListViewModel::class)
    internal abstract fun somListViewModel(viewModel: SomListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SomFilterViewModel::class)
    internal abstract fun somFilterViewModel(viewModel: SomFilterViewModel): ViewModel
}