package com.tokopedia.thankyou_native.presentation

import android.app.Activity
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

class DialogController(private val presenter: GratificationPresenter) {

    var job: Job? = null

    fun showGratifDialog(weakReference: WeakReference<Activity>,
                         paymentId: Long,
                         gratifPopupCallback: GratificationPresenter.GratifPopupCallback,
                         screenName: String
    ): Job? {
        job = presenter.showGratificationInApp(weakActivity = weakReference,
                paymentID = paymentId,
                gratifPopupCallback = gratifPopupCallback,
                notificationEntryType = NotificationEntryType.ORGANIC,
                screenName = screenName,
                timeout = 6000L)
        return job
    }

    fun cancelJob() {
        job?.cancel()
    }
}