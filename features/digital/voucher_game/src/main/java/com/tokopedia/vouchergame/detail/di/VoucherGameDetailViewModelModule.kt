package com.tokopedia.vouchergame.detail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import com.tokopedia.vouchergame.list.view.viewmodel.VoucherGameListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 13/08/19
 */
@Module
@VoucherGameDetailScope
abstract class VoucherGameDetailViewModelModule {

    @VoucherGameDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VoucherGameDetailViewModel::class)
    internal abstract fun voucherGameDetailViewModel(viewModel: VoucherGameDetailViewModel): ViewModel
}