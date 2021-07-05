package com.tokopedia.settingnotif.usersetting.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_SETTINGS
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.settingnotif.usersetting.const.Unify.Unify_N700_68
import com.tokopedia.settingnotif.usersetting.const.Unify.Unify_N700_96
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*

/**
 * open notification system settings,
 * only supported for O and above
 * @return Intent
 */
fun Context.notificationSetting(): Intent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    } else {
        Intent(ACTION_SETTINGS)
    }
}

/**
 * extension for Rx subscriber with
 * return two callback
 * @param requestParams
 * @param onSuccess
 * @param onError
 */
fun <T> UseCase<T>.load(
        requestParams: RequestParams = RequestParams.EMPTY,
        onSuccess: (t: T?) -> Unit,
        onError: (err: Throwable?) -> Unit
) {
    execute(requestParams, object : Subscriber<T>() {
        override fun onNext(t: T) = onSuccess(t)
        override fun onError(e: Throwable?) = onError(e)
        override fun onCompleted() {}
    })
}

/**
 * change user info for email and phone number
 * @param appLink
 * @param email
 * @param phoneNumber
 * @return Intent
 */
fun Context.changeUserInfoIntent(
        appLink: String,
        email: String,
        phoneNumber: String
): Intent {
    return intent(appLink).apply {
        putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
    }
}

/**
 * extension for simplify get intent from appLink
 * @param appLink
 * @return Intent
 */
fun Context.intent(appLink: String): Intent {
    return RouteManager.getIntent(this, appLink)
}

/**
 * simplify inflate a view
 * @param layoutId
 * @return View
 */
fun Context?.inflateView(@LayoutRes layoutId: Int): View {
    return View.inflate(this, layoutId, null)
}

/**
 * Component text color based on isEnabled
 * switch any view colors with grey if component is disabled,
 * and switch the color back if component is enabled
 */
fun componentTextColor(isEnabled: Boolean): Int {
    return if (isEnabled) Unify_N700_96 else Unify_N700_68
}


/**
 * cloning a data class object
 * using json converter utilities
 */
inline fun <reified T> dataClone(src: Any): T {
    val json = CommonUtils.toJson(src)
    return CommonUtils.fromJson(json, T::class.java)
}

/**
 * date formatter for last troubleshooter check
 * @return: Long to String
 * */
fun Long.toLastCheckFormat(): String {
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return df.format(this)
}