package com.tokopedia.sessioncommon.util

import android.app.Activity
import android.content.Context
import android.content.Intent

object LoginSdkUtils {

    private const val LOGIN_SDK_PREF = "lsdk_pref"
    private const val PREF_KEY_CLIENT_NAME = "SDK_CLIENT_NAME"
    private const val RESULT_AUTH_CODE = "auth_code"
    private const val RESULT_ERROR = "error"
    private const val RESULT_ERROR_CODE = "error_code"
    const val ERR_CODE_UNKNOWN = "err_unknown"
    const val ERR_CODE_API = "api_error"
    const val ERR_CODE_CLIENT = "client_error"
    const val ERR_CLIENT_CANCELLED = "User click on back button"

    fun redirectToTargetUri(activity: Activity, redirectUrl: String, authCode: String, error: String = "", errorCode: String = "") {
        val intent = Intent()
        if (authCode.isNotEmpty()) {
            intent.putExtra(RESULT_AUTH_CODE, authCode)
        }
        if (error.isNotEmpty()) {
            intent.putExtra(RESULT_ERROR, error)
            intent.putExtra(RESULT_ERROR_CODE, errorCode.ifEmpty { ERR_CODE_UNKNOWN })
        }
        activity.removeLoginSdkFlow()
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    fun getClientLabelIfAvailable(clientName: String, removeDash: Boolean = false): String {
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
