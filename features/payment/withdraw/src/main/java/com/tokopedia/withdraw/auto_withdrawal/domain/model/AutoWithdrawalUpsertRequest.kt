package com.tokopedia.withdraw.auto_withdrawal.domain.model

import com.google.gson.annotations.SerializedName

data class AutoWithdrawalUpsertRequest (
        val autoWDStatusData: AutoWDStatusData,
        val oldSchedule: Schedule?,
        val newSchedule: Schedule?,
        val bankAccount: BankAccount?,
        val isUpdating: Boolean,
        var token: String?,
        val isQuit: Boolean
)