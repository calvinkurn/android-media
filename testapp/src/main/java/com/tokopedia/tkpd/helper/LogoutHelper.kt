package com.tokopedia.tkpd.helper

import android.widget.Toast
import com.tokopedia.application.MyApplication
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.tkpd.network.DataSource
import com.tokopedia.tkpd.network.LogoutPojo
import com.tokopedia.user.session.UserSession
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

fun logout(application: MyApplication) {
    val userSession = UserSession(application)
    DataSource.getWsLogoutService(application).logout(
            mapOf(
                    "user_id" to userSession.userId.toString(),
                    "device_id" to DataSource.MOCK_DEVICE_ID,
                    "hash" to AuthHelper.getMD5Hash(userSession.userId.toString() + "~" + DataSource.MOCK_DEVICE_ID),
                    "os_type" to "1",
                    "device_time" to (Date().time / 1000).toString()
            )
    ).map { logoutPojo ->
        if (logoutPojo?.data == null) {
            throw (RuntimeException("Error logout"))
        } else {
            logoutPojo
        }
    }.doOnNext {
        userSession.logoutSession()
        PersistentCacheManager.instance.delete()
    }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LogoutPojo> {
                override fun onError(e: Throwable?) {
                    Toast.makeText(application, e?.message, Toast.LENGTH_LONG).show()
                }

                override fun onNext(t: LogoutPojo?) {
                    Toast.makeText(application, "success logout", Toast.LENGTH_LONG).show()
                }

                override fun onCompleted() {

                }
            })

}