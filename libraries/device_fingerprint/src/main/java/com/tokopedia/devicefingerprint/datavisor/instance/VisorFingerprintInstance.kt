package com.tokopedia.devicefingerprint.datavisor.instance

import android.content.Context
import com.datavisor.vangogh.face.DVTokenClient
import com.tokopedia.devicefingerprint.datavisor.`object`.VisorObject

class VisorFingerprintInstance {

    lateinit var visorInstance : DVTokenClient
    private var visorToken = ""
    private var errorCode = 0


    private fun getVisorInstance(applicationContext: Context) {
        visorInstance = DVTokenClient.getInstance(applicationContext)
        visorInstance.setDVCustomDomain(VisorObject.Key.APP_DOMAIN)
    }

    fun initToken(applicationContext: Context, customUserDimension: Map<String, String>? = null, listener: onVisorInitListener? = null ) {
        if (!this::visorInstance.isInitialized) {
            getVisorInstance(applicationContext)
        }
        visorInstance.initToken(VisorObject.Key.APP_KEY, VisorObject.Key.APP_SECRET, customUserDimension) { strToken, nResultCode ->
            if (nResultCode == 0) {
                visorToken = strToken
                listener?.onSuccessInitToken(token = strToken)
            } else {
                errorCode = nResultCode
                listener?.onFailedInitToken(error = "failed to init visor token code : " + nResultCode)
            }
        }
    }

    fun getVisorToken(): String {
        return when {
            visorToken.isNotEmpty() -> visorToken
            this::visorInstance.isInitialized -> visorInstance.dvToken
            else -> ""
        }
    }

    interface onVisorInitListener {
        fun onSuccessInitToken(token: String)
        fun onFailedInitToken(error: String)
    }

}