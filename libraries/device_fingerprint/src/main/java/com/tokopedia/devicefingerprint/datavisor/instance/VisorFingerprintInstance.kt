package com.tokopedia.devicefingerprint.datavisor.instance

import android.content.Context
import com.datavisor.vangogh.face.DVTokenClient
import com.tokopedia.devicefingerprint.datavisor.`object`.VisorObject
import com.tokopedia.encryption.security.sha256

class VisorFingerprintInstance {

    companion object {
        var visorInstance : DVTokenClient? = null
        private var visorToken = ""
        private var isTokenInit = false
        const val KEY_USER_ID = "HASHED_USER_ID"

        private fun getVisorInstance(applicationContext: Context): DVTokenClient{
            if (visorInstance == null) {
                visorInstance = DVTokenClient.getInstance(applicationContext)
                visorInstance?.setDVCustomDomain(VisorObject.Key.APP_DOMAIN)
            }
            return visorInstance!!
        }

        fun initToken(context: Context, userId:String, listener: onVisorInitListener? = null ) {
            if (!isTokenInit) {
                val customUserDimension = mapOf(KEY_USER_ID to userId.sha256())
                getVisorInstance(context.applicationContext).initToken(VisorObject.Key.APP_KEY, VisorObject.Key.APP_SECRET, customUserDimension) { strToken, nResultCode ->
                    if (nResultCode == 0) {
                        isTokenInit = true
                        visorToken = strToken
                        listener?.onSuccessInitToken(token = strToken)
                    } else {
                        listener?.onFailedInitToken(error = "failed to init visor token code : $nResultCode")
                    }
                }
            } else {
                listener?.onSuccessInitToken(visorToken)
            }
        }
    }

    interface onVisorInitListener {
        fun onSuccessInitToken(token: String)
        fun onFailedInitToken(error: String)
    }

}