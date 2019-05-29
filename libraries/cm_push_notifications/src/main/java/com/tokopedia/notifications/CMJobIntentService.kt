package com.tokopedia.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.JobIntentService
import android.util.Log

import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.factory.BaseNotification
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.notifications.common.CMEvents
import com.tokopedia.notifications.common.CMNotificationUtils
import java.util.*
import kotlin.collections.HashMap


/**
 * @author lalit.singh
 */
class CMJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        try {
            val bundle = intent.getBundleExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE)
            if (null != bundle) {

                val baseNotification = CMNotificationFactory.getNotification(this.applicationContext, bundle)
                if (null != baseNotification)
                    postNotification(baseNotification)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }

    private fun postNotification(baseNotification: BaseNotification) {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = baseNotification.createNotification()
        notificationManager.notify(baseNotification.baseNotificationModel.notificationId, notification)
    }



    companion object {

        private val JOB_ID = 131

        internal var TAG = CMJobIntentService::class.java.simpleName

        @JvmStatic
        fun enqueueWork(context: Context, bundle: Bundle) {
            val work = Intent()
            work.putExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE, bundle)
            enqueueWork(context, CMJobIntentService::class.java, JOB_ID, work)
        }
    }

}
