package com.tokopedia.withdraw.saldowithdrawal.presentation.listener

import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest

interface WithdrawalFragmentCallback {
    fun openSuccessFragment(withdrawalRequest: WithdrawalRequest,
                            submitWithdrawalResponse: SubmitWithdrawalResponse)
}