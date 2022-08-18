package com.tokopedia.saldodetails.commom.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object SaldoDetailsRollenceUtil {

    fun shouldShowModalTokoWidget(context: Context): Boolean {
        return getShouldShowModalTokoWidget(context)
    }

    private fun getShouldShowModalTokoWidget(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        return config.getBoolean(RemoteConfigKey.SHOW_MODAL_TOKO_WIDGET_SALDO,false)
    }

}





