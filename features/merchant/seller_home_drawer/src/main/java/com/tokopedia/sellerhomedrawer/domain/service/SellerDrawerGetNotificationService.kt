package com.tokopedia.sellerhomedrawer.domain.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase
import rx.Subscriber
import javax.inject.Inject

class SellerDrawerGetNotificationService : JobIntentService() {

    companion object {
        @JvmStatic
        val JOB_ID = 12383213
        @JvmStatic
        val BROADCAST_GET_NOTIFICATION = "broadcast_get_notification"
        @JvmStatic
        val GET_NOTIFICATION_SUCCESS = "get_notification_success"
        @JvmStatic
        private val KEY_IS_SELLER = "is_seller"
        @JvmStatic
        private val KEY_IS_REFRESH = "is_refresh"

        @JvmStatic
        fun startService(context: Context, isRefresh: Boolean) {
            val work = Intent(context, SellerDrawerGetNotificationService::class.java)
            //KEY_IS_SELLER is always true as this class is only used in sellerapp
            work.putExtra(KEY_IS_SELLER, true)
            work.putExtra(KEY_IS_REFRESH, isRefresh)
            enqueueWork(context, SellerDrawerGetNotificationService::class.java, JOB_ID, work)
        }
    }

    @Inject
    lateinit var newNotificationUseCase: NewNotificationUseCase

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHandleWork(intent: Intent) {
        val isSeller = intent.getBooleanExtra(KEY_IS_SELLER, false)
        val isRefresh = intent.getBooleanExtra(KEY_IS_REFRESH, false)
        newNotificationUseCase.setRefresh(isRefresh)
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(isSeller), object : Subscriber<NotificationModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(notificationModel: NotificationModel) {
                if (notificationModel.isSuccess)
                    sendBroadcast()
            }
        })
    }

    private fun sendBroadcast() {
        val intent = Intent(BROADCAST_GET_NOTIFICATION)
        intent.putExtra(GET_NOTIFICATION_SUCCESS, true)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }
}