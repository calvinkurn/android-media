package com.tokopedia.sessioncommon.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

object LoginSdkUtils {
    fun redirectToTargetUri(activity: Activity, redirectUrl: String, authCode: String, error: String = "") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
        if (authCode.isNotEmpty()) {
            intent.putExtra("auth_code", authCode)
        }
        if (error.isNotEmpty()) {
            intent.putExtra("error", error)
        }
        activity.removeLoginSdkFlow()
        activity.startActivity(intent)
        activity.finish()
    }

    fun getClientLabelIfAvailable(clientName: String) : String {
        return if (clientName.isNotEmpty()) {
            " - from $clientName"
        } else ""
    }

    fun Context.setAsLoginSdkFlow(clientName: String) {
        this.getSharedPreferences("LOGIN_SDK_PREF", Context.MODE_PRIVATE).edit().putString("SDK_CLIENT_NAME", clientName).apply()
    }

    fun Context.removeLoginSdkFlow() {
        this.getSharedPreferences("LOGIN_SDK_PREF", Context.MODE_PRIVATE).edit().remove("SDK_CLIENT_NAME").apply()
    }

    fun Context.isLoginSdkFlow(): Boolean =
        this.getSharedPreferences("LOGIN_SDK_PREF", Context.MODE_PRIVATE).getString("SDK_CLIENT_NAME", "")?.isNotEmpty() == true

    fun Context.getClientName(): String =
        this.getSharedPreferences("LOGIN_SDK_PREF", Context.MODE_PRIVATE).getString("SDK_CLIENT_NAME", "") ?: ""
}
