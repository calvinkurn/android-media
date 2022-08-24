package com.tokopedia.saldodetails.commom.utils

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey

object SaldoDetailsRollenceUtil {

    fun shouldShowModalTokoWidget(): Boolean {
        return getShouldShowModalTokoWidget()
    }

    private fun getShouldShowModalTokoWidget(): Boolean {
        val config: RemoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
        val saldoModalTokoWidgetData = config.getString(RemoteConfigKey.SALDO_MODAL_TOKO_WIDGET)
        return saldoModalTokoWidgetData != RemoteConfigKey.SALDO_MODAL_TOKO_WIDGET
    }

}





