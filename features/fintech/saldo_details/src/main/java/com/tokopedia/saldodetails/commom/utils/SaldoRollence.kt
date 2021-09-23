package com.tokopedia.saldodetails.commom.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance

object SaldoRollence {

    private const val KEY_SALDO_REVAMP = "saldo_history_revamp"

    fun isSaldoRevampEnabled() : Boolean = (KEY_SALDO_REVAMP == RemoteConfigInstance.getInstance()
            .abTestPlatform.getString(KEY_SALDO_REVAMP, ""))
}