package com.tokopedia.withdraw.saldowithdrawal.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.withdraw.saldowithdrawal.di.scope.WithdrawScope
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.BankAccountListViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.RekeningPremiumViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.ValidatePopUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@WithdrawScope
@Module
abstract class ViewModelModule {

    @WithdrawScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BankAccountListViewModel::class)
    internal abstract fun bankAccountListViewModel(viewModel: BankAccountListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RekeningPremiumViewModel::class)
    internal abstract fun rekeningPremiumViewModel(viewModel: RekeningPremiumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ValidatePopUpViewModel::class)
    internal abstract fun validatePopUpViewModel(viewModel: ValidatePopUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmitWithdrawalViewModel::class)
    internal abstract fun provideSubmitWithdrawalViewModel(viewModel: SubmitWithdrawalViewModel): ViewModel

}