package com.tokopedia.devicefingerprint.datavisor.instance

import android.content.Context
import com.datavisor.vangogh.face.DVTokenClient
import com.tokopedia.devicefingerprint.datavisor.`object`.VisorObject

class VisorFingerprintInstance {

    companion object {
        var visorInstance : DVTokenClient? = null
        private var visorToken = ""
        private var isTokenInit = false

        private fun getVisorInstance(applicationContext: Context): DVTokenClient{
            if (visorInstance == null) {
                visorInstance = DVTokenClient.getInstance(applicationContext)
                visorInstance?.setDVCustomDomain(VisorObject.Key.APP_DOMAIN)
            }
            return visorInstance!!
        }

        fun initToken(applicationContext: Context, customUserDimension: Map<String, String>? = null, listener: onVisorInitListener ) {
            if (!isTokenInit) {
                getVisorInstance(applicationContext).initToken(VisorObject.Key.APP_KEY, VisorObject.Key.APP_SECRET, customUserDimension) { strToken, nResultCode ->
                    if (nResultCode == 0) {
                        isTokenInit = true
                        visorToken = strToken
                        listener.onSuccessInitToken(token = strToken)
                    } else {
                        listener.onFailedInitToken(error = "failed to init visor token code : " + nResultCode)
                    }
                }
            }
        }

        fun getVisorToken(): String {
            return if(visorToken.isNotEmpty()) visorToken
            else visorInstance?.dvToken ?: ""
        }
    }

    interface onVisorInitListener {
        fun onSuccessInitToken(token: String)
        fun onFailedInitToken(error: String)
    }

}