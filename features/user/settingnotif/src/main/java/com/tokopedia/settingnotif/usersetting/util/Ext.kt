package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
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

fun <T> UseCase<T>.load(onSuccess: (t: T?) -> Unit, onError: () -> Unit) {
    execute(object : Subscriber<T>() {
        override fun onNext(t: T) {
            onSuccess(t)
        }

        override fun onError(e: Throwable?) {
            onError()
        }

        override fun onCompleted() {}
    })
}
