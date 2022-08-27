package com.tokopedia.saldodetails.commom.utils

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object SaldoDetailsRollenceUtil {
    fun shouldShowModalTokoWidget(): Boolean {
        return getShouldShowModalTokoWidget()
    }

    private fun getShouldShowModalTokoWidget(): Boolean {
        val config: RemoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
        val saldoModalTokoWidgetData = config.getString(RollenceKey.SALDO_MODAL_TOKO_WIDGET)
        return saldoModalTokoWidgetData != RollenceKey.SALDO_MODAL_TOKO_WIDGET
    }
}