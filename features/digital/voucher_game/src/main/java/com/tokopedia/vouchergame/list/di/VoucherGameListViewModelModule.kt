package com.tokopedia.vouchergame.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 13/08/19
 */
@Module
@VoucherGameListScope
abstract class VoucherGameListViewModelModule {

    @VoucherGameListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VoucherGameListViewModel::class)
    internal abstract fun voucherGameListViewModel(viewModel: VoucherGameListViewModel): ViewModel
}