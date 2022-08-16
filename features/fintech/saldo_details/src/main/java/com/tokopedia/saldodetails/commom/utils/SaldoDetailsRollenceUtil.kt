package com.tokopedia.saldodetails.commom.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object SaldoDetailsRollenceUtil {

    private var isShowModalTokoWidget: Boolean? = null

    fun shouldShowModalTokoWidget(context: Context): Boolean {
        return isShowModalTokoWidget ?: getShouldShowModalTokoWidget(context)
    }

    //todo change RemoteConfigKey
    private fun getShouldShowModalTokoWidget(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        isShowModalTokoWidget =
            config.getBoolean(RemoteConfigKey.SHOW_MODAL_TOKO_WIDGET_SALDO, false)
        return isShowModalTokoWidget ?: false
    }

}





