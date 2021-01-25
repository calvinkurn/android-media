package com.tokopedia.saldodetails.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.saldodetails.viewmodels.SaldoHistoryViewModel
import com.tokopedia.saldodetails.viewmodels.MerchantSaldoPriorityViewModel
import com.tokopedia.saldodetails.viewmodels.SaldoDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @SaldoDetailsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(SaldoDetailViewModel::class)
    internal abstract fun SaldoDetailVM(viewModel: SaldoDetailViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(MerchantSaldoPriorityViewModel::class)
    internal abstract fun MerchantSPViewModel(viewModel: MerchantSaldoPriorityViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(SaldoHistoryViewModel::class)
    internal abstract fun SaldoHistoryViewModel(viewModel: SaldoHistoryViewModel): ViewModel
}