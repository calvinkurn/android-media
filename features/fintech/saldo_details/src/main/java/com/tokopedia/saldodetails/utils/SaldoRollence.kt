package com.tokopedia.saldodetails.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance

object SaldoRollence {

    private val KEY_SALDO_REVAMP = "saldo_history_revamp"

    fun isSaldoRevampEnabled() : Boolean = true/*(KEY_SALDO_REVAMP == RemoteConfigInstance.getInstance()
            .abTestPlatform.getString(KEY_SALDO_REVAMP, ""))*/

}