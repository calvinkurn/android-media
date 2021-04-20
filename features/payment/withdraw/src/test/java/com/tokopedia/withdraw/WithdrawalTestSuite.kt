package com.tokopedia.withdraw

import com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel.AutoWDSettingsViewModelTest
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.JoinRekeningTermsConditionViewModelTest
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.RekeningPremiumViewModelTest
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SaldoWithdrawalViewModelTest
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        JoinRekeningTermsConditionViewModelTest::class,
        RekeningPremiumViewModelTest::class,
        SaldoWithdrawalViewModelTest::class,
        SubmitWithdrawalViewModelTest::class,
        AutoWDSettingsViewModelTest::class)
class WithdrawalTestSuite {
}