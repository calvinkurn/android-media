package com.tokopedia.thankyou_native.presentation

import android.app.Activity
import android.os.Debug
import android.util.Log
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import kotlinx.coroutines.Job
import timber.log.Timber
import java.lang.ref.WeakReference

const val GRATIF_TIMEOUT = 7000L

class DialogController(private val presenter: GratificationPresenter) {

    var job: Job? = null

    fun showGratifDialog(weakReference: WeakReference<Activity>,
                         paymentId: Long,
                         gratifPopupCallback: GratificationPresenter.GratifPopupCallback,
                         screenName: String
    ): Job? {
        val curTime = System.currentTimeMillis()
        val TAG = "GratifTag"
        Log.e(TAG,"gratification start time seconds:${curTime/1000}")
        Debug.startMethodTracing("gratification")
        job = presenter.showGratificationInApp(weakActivity = weakReference,
                paymentID = paymentId,
                gratifPopupCallback = gratifPopupCallback,
                notificationEntryType = NotificationEntryType.ORGANIC,
                screenName = screenName,
                timeout = GRATIF_TIMEOUT, closeCurrentActivity = true)
        return job
    }

    fun cancelJob() {
        job?.cancel()
    }
}