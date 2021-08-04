package com.tokopedia.saldodetails.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity

object SaldoRollence {

    val KEY_SALDO_REVAMO = "saldo_history_revamp"

    fun isSaldoRevampEnabled() : Boolean = (KEY_SALDO_REVAMO == RemoteConfigInstance.getInstance()
            .abTestPlatform.getString(KEY_SALDO_REVAMO, ""))

}