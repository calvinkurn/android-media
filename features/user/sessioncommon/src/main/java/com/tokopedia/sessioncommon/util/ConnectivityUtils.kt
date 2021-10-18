package com.tokopedia.sessioncommon.util

/**
 * Created by Yoris on 18/10/21.
 */

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Method

/**
 * Created by Yoris on 18/10/21.
 */

object ConnectivityUtils {

    fun isSilentVerificationPossible(context: Context?): Boolean {
        return if(context != null)
            isMobileDataEnabled(context) && isSimCardReady(context)
        else false
    }

    fun isMobileDataEnabled(context: Context): Boolean {
        val connectivityService = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
        val cm = connectivityService as ConnectivityManager
        return try {
            val c = Class.forName(cm.javaClass.name)
            val m: Method = c.getDeclaredMethod("getMobileDataEnabled")
            m.invoke(cm) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getSimCardStatus(context: Context): Int {
        return try {
            val telMgr = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
            telMgr.simState
        } catch (e: Exception) {
            TelephonyManager.SIM_STATE_UNKNOWN
        }
    }

    fun isSimCardReady(context: Context): Boolean {
        return getSimCardStatus(context) == TelephonyManager.SIM_STATE_READY
//        when (simState) {
//            TelephonyManager.SIM_STATE_ABSENT -> {
//                text?.text = "TelephonyManager.SIM_STATE_ABSENT"
//            }
//            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {
//                text?.text = "TelephonyManager.SIM_STATE_NETWORK_LOCKED"
//            }
//            TelephonyManager.SIM_STATE_PIN_REQUIRED -> {
//                text?.text = "TelephonyManager.SIM_STATE_PIN_REQUIRED"
//            }
//            TelephonyManager.SIM_STATE_PUK_REQUIRED -> {
//                text?.text = "TelephonyManager.SIM_STATE_PUK_REQUIRED"
//            }
//            TelephonyManager.SIM_STATE_READY -> {
//                text?.text = "TelephonyManager.SIM_STATE_READY"
//            }
//            TelephonyManager.SIM_STATE_UNKNOWN -> {
//                text?.text = "TelephonyManager.SIM_STATE_UNKNOWN"
//            }
//        }

    }
}