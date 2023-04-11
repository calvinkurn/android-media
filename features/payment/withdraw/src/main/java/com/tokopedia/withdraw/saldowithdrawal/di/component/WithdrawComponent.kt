package com.tokopedia.withdraw.saldowithdrawal.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.withdraw.saldowithdrawal.di.module.GqlQueryModule
import com.tokopedia.withdraw.saldowithdrawal.di.module.ViewModelModule
import com.tokopedia.withdraw.saldowithdrawal.di.module.WithdrawModule
import com.tokopedia.withdraw.saldowithdrawal.di.scope.WithdrawScope
import com.tokopedia.withdraw.saldowithdrawal.presentation.activity.WithdrawActivity
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.DisabledAccountBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.JoinRPOnWithdrawalBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.RekPremBankAccountInfoBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.BaseWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.ThankYouFragmentWithdrawal
import dagger.Component

/**
 * @author by StevenFredian on 30/07/18.
 */
@WithdrawScope
@Component(modules = [WithdrawModule::class,
    ViewModelModule::class,
    GqlQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface WithdrawComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(withdrawActivity: WithdrawActivity)
    fun inject(thankYouFragmentWithdrawal: ThankYouFragmentWithdrawal)
    fun inject(baseWithdrawalFragment: BaseWithdrawalFragment)
    fun inject(withdrawalParentFragment: SaldoWithdrawalFragment)
    fun inject(joinRPOnWithdrawalBottomSheet: JoinRPOnWithdrawalBottomSheet)
    fun inject(rekPremBankAccountInfoBottomSheet: RekPremBankAccountInfoBottomSheet)
    fun inject(disabledAccountBottomSheet: DisabledAccountBottomSheet)
}
