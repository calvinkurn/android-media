package com.tokopedia.saldodetails.commom.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object SaldoDetailsRollenceUtil {

    fun shouldShowModalTokoWidget(): Boolean {
        return getShouldShowModalTokoWidget()
    }

    private fun getShouldShowModalTokoWidget(): Boolean {
       return RemoteConfigInstance.getInstance().abTestPlatform.run{
            getString(RollenceKey.SALDO_MODAL_TOKO_WIDGET) != RollenceKey.SALDO_MODAL_TOKO_WIDGET
        }
    }
}