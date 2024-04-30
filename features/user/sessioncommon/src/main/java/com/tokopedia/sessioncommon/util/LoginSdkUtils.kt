package com.tokopedia.sessioncommon.util

import android.app.Activity
import android.content.Context
import android.content.Intent

object LoginSdkUtils {

    private const val LOGIN_SDK_PREF = "lsdk_pref"
    private const val PREF_KEY_CLIENT_NAME = "SDK_CLIENT_NAME"
    private const val RESULT_AUTH_CODE = "auth_code"
    private const val RESULT_ERROR = "error"

    fun redirectToTargetUri(activity: Activity, redirectUrl: String, authCode: String, error: String = "") {
//        val finalUrl = if (inQuery) "$redirectUrl&auth_code=$authCode&error=$error" else redirectUrl
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
//        if (!inQuery) {
//            if (authCode.isNotEmpty()) {
//                intent.putExtra(RESULT_AUTH_CODE, authCode)
//            }
//            if (error.isNotEmpty()) {
//                intent.putExtra(RESULT_ERROR, error)
//            }
//        }

        val intent = Intent()
        if (authCode.isNotEmpty()) {
            intent.putExtra(RESULT_AUTH_CODE, authCode)
        }
        if (error.isNotEmpty()) {
            intent.putExtra(RESULT_ERROR, error)
        }

        activity.removeLoginSdkFlow()
        activity.setResult(Activity.RESULT_OK, intent)
//        activity.startActivity(intent)
//        activity.finish()
    }

    fun getClientLabelIfAvailable(clientName: String, removeDash: Boolean = false) : String {
        return if (clientName.isNotEmpty()) {
            if (removeDash) {
                clientName
            } else " - $clientName"
        } else ""
    }

    fun Context.setAsLoginSdkFlow(clientName: String) {
        this.getSharedPreferences(LOGIN_SDK_PREF, Context.MODE_PRIVATE).edit().putString(PREF_KEY_CLIENT_NAME, clientName).apply()
    }

    fun Context.removeLoginSdkFlow() {
        this.getSharedPreferences(LOGIN_SDK_PREF, Context.MODE_PRIVATE).edit().remove(PREF_KEY_CLIENT_NAME).apply()
    }

    fun Context.isLoginSdkFlow(): Boolean =
        this.getSharedPreferences(LOGIN_SDK_PREF, Context.MODE_PRIVATE).getString(PREF_KEY_CLIENT_NAME, "")?.isNotEmpty() == true

    fun Context.getClientName(): String =
        this.getSharedPreferences(LOGIN_SDK_PREF, Context.MODE_PRIVATE).getString(PREF_KEY_CLIENT_NAME, "") ?: ""
}
