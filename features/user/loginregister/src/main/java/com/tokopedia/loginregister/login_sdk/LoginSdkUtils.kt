package com.tokopedia.loginregister.login_sdk

import android.app.Activity
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
        activity.startActivity(intent)
        activity.finish()
    }
}
