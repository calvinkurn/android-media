package com.tokopedia.devicefingerprint.datavisor.instance

import android.content.Context
import com.datavisor.vangogh.face.DVTokenClient
import com.tokopedia.devicefingerprint.datavisor.`object`.VisorObject
import com.tokopedia.encryption.security.sha256

class VisorFingerprintInstance {

    companion object {
        var visorInstance: DVTokenClient? = null
        private var visorToken = ""
        private var isTokenInit = false
        const val KEY_USER_ID = "HASHED_USER_ID"

        const val SHARED_PREF_NAME = "pref_dv"
        const val KEY_TOKEN = "tk"
        const val DEFAULT_VALUE_DATAVISOR = "DVLT_6542b775e3263c27e321b929-f52fc6e0_dFlt"

        private fun getVisorInstance(applicationContext: Context): DVTokenClient {
            if (visorInstance == null) {
                visorInstance = DVTokenClient.getInstance(applicationContext)
                visorInstance?.setDVCustomDomain(VisorObject.Key.APP_DOMAIN)
            }
            return visorInstance!!
        }

        fun initToken(context: Context, userId: String, listener: onVisorInitListener? = null) {
            if (!isTokenInit) {
                val ctx = context.applicationContext
                val customUserDimension = mapOf(KEY_USER_ID to userId.sha256())
                getVisorInstance(ctx).initToken(VisorObject.Key.APP_KEY, VisorObject.Key.APP_SECRET, customUserDimension) { strToken, nResultCode ->
                    if (nResultCode == 0) {
                        isTokenInit = true
                        visorToken = strToken
                        setDVToken(ctx, visorToken)
                        listener?.onSuccessInitToken(token = strToken)
                    } else {
                        listener?.onFailedInitToken(error = "failed to init visor token code : $nResultCode")
                    }
                }
            } else {
                listener?.onSuccessInitToken(visorToken)
            }
        }

        fun setDVToken(context: Context, token: String) {
            val sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            sp.edit().putString(KEY_TOKEN, token).apply()
        }

        fun getDVToken(context: Context): String {
            val sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sp.getString(KEY_TOKEN, DEFAULT_VALUE_DATAVISOR) ?: DEFAULT_VALUE_DATAVISOR
        }
    }

    interface onVisorInitListener {
        fun onSuccessInitToken(token: String)
        fun onFailedInitToken(error: String)
    }

}