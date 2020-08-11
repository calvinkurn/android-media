package com.tokopedia.withdraw.saldowithdrawal.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.JoinRekeningTermsConditionViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.RekeningPremiumViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SaldoWithdrawalViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SaldoWithdrawalViewModel::class)
    internal abstract fun saldoWithdrawalViewModel(viewModel: SaldoWithdrawalViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RekeningPremiumViewModel::class)
    internal abstract fun rekeningPremiumViewModel(viewModel: RekeningPremiumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmitWithdrawalViewModel::class)
    internal abstract fun provideSubmitWithdrawalViewModel(viewModel: SubmitWithdrawalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JoinRekeningTermsConditionViewModel::class)
    internal abstract fun provideJoinRekeningTermsConditionViewModel(viewModel: JoinRekeningTermsConditionViewModel): ViewModel

}