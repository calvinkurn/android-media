package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Subscriber

fun Context.openNotificationSetting() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }
}

fun <T> UseCase<T>.load(
        requestParams: RequestParams = RequestParams.EMPTY,
        onSuccess: (t: T?) -> Unit,
        onError: () -> Unit
) {
    execute(requestParams, object : Subscriber<T>() {
        override fun onNext(t: T) {
            onSuccess(t)
        }

        override fun onError(e: Throwable?) {
            onError()
        }

        override fun onCompleted() {}
    })
}

fun Context.changeUserInfoIntent(
        appLink: String,
        email: String,
        phoneNumber: String
): Intent {
    return RouteManager.getIntent(this, appLink).apply {
        putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
    }
}
