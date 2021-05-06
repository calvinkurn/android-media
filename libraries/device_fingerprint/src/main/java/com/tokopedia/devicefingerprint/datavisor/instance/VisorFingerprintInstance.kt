package com.tokopedia.devicefingerprint.datavisor.instance

import android.content.Context
import com.datavisor.vangogh.face.DVTokenClient
import com.tokopedia.devicefingerprint.datavisor.`object`.VisorObject
import com.tokopedia.encryption.security.sha256
import com.tokopedia.encryption.utils.Utils.decodeDecimalToText

class VisorFingerprintInstance {

    companion object {
        var visorInstance: DVTokenClient? = null
        private var visorToken = ""
        private var isTokenInit = false
        const val KEY_USER_ID = "HASHED_USER_ID"

        const val DV_SHARED_PREF_NAME = "pref_dv"
        const val KEY_TOKEN = "tk"
        val DEFAULT_VALUE_DATAVISOR = decodeDecimalToText(intArrayOf(68, 86, 76, 84, 95, 54, 53, 52, 50, 98, 55, 55, 53, 101, 51, 50, 54, 51, 99, 50, 55, 101, 51, 50, 49, 98, 57, 50, 57, 45, 102, 53, 50, 102, 99, 54, 101, 48, 95, 100, 70, 108, 116))

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
            val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            sp.edit().putString(KEY_TOKEN, token).apply()
        }

        fun getDVToken(context: Context): String {
            val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sp.getString(KEY_TOKEN, DEFAULT_VALUE_DATAVISOR) ?: DEFAULT_VALUE_DATAVISOR
        }
    }

    interface onVisorInitListener {
        fun onSuccessInitToken(token: String)
        fun onFailedInitToken(error: String)
    }

}