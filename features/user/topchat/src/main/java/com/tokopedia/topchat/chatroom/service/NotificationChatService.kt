package com.tokopedia.topchat.chatroom.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatUseCase
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import rx.Subscriber
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationChatService : JobIntentService() {

    private val REPLY_KEY = "reply_chat_key"
    private val MESSAGE_ID = "message_chat_id"
    private val NOTIFICATION_ID = "notification_id"
    private val USER_ID = "user_id"

    @Inject
    lateinit var replyChatUseCase: ReplyChatUseCase

    @Inject
    lateinit var analytics: TopChatAnalytics

    private var jobScheduler: JobScheduler? = null
    private var remoteConfig: RemoteConfig? = null

    companion object {
        private const val JOB_ID_RETRY = 712
        private const val JOB_ID_NOTIFICATION = 812
        private const val DELAY_THREAD_BINDER = 1000L

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NotificationChatService::class.java, JOB_ID_NOTIFICATION, intent)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)?.also {
            Handler().post {
                Thread.sleep(DELAY_THREAD_BINDER)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .chatRoomContextModule(ChatRoomContextModule(this))
                .build()
                .inject(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                    ?: return
        }

        remoteConfig = FirebaseRemoteConfigImpl(this)
    }

    override fun onHandleWork(intent: Intent) {
        executeReplyChat(intent)
    }

    private fun executeReplyChat(intent: Intent) {

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        val message = if (intent.getStringExtra(REPLY_KEY).isNullOrEmpty()) {
            remoteInput?.getCharSequence(REPLY_KEY).toString()
        } else {
            intent.getStringExtra(REPLY_KEY)
        }

        val messageId = intent.getStringExtra(MESSAGE_ID).orEmpty()
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        val userId = intent.getStringExtra(USER_ID)

        val params = ReplyChatUseCase.generateParamWithSource(messageId, message, TopChatAnalytics.SELLERAPP_PUSH_NOTIF)

        replyChatUseCase.execute(params, object : Subscriber<ReplyChatViewModel>() {
            override fun onNext(response: ReplyChatViewModel) {
                if (response.isSuccessReplyChat) {
                    analytics.eventClickReplyChatFromNotif(if (userId.isNullOrBlank()) "0" else userId)
                    clearNotification(notificationId)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (isJobIdRunning(JOB_ID_RETRY)) {
                            jobScheduler?.cancel(JOB_ID_RETRY)
                            applicationContext.stopService(Intent(applicationContext, NotificationChatJobService::class.java))
                        }
                    }
                } else {
                    onError(IllegalStateException())
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                if (!DeviceConnectionInfo.isInternetAvailable(applicationContext,
                                checkWifi = true,
                                checkCellular = true,
                                checkEthernet = true)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        jobScheduler?.cancelAll()
                        if (isEnableReplyChatNotification()) {
                            setRetryJob(messageId, message, notificationId, if (userId.isNullOrBlank()) "0" else userId)
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        jobScheduler?.cancelAll()
                    }
                    ServerLogger.log(Priority.P2, "PUSH_NOTIF_REPLY_CHAT", mapOf("type" to "ErrorReplyChat", "error" to e?.message.orEmpty()))
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRetryJob(messageId: String,
                            message: String,
                            notificationId: Int,
                            userId: String) {

        val bundle = PersistableBundle()
        bundle.putString(MESSAGE_ID, messageId)
        bundle.putString(REPLY_KEY, message)
        bundle.putInt(NOTIFICATION_ID, notificationId)
        bundle.putString(USER_ID, userId)
        val minDelay = TimeUnit.SECONDS.toMillis(3)
        val maxDelay = TimeUnit.MINUTES.toMillis(2)

        jobScheduler?.schedule(
                JobInfo.Builder(JOB_ID_RETRY,
                        ComponentName(applicationContext, NotificationChatJobService::class.java))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setMinimumLatency(minDelay)
                        .setOverrideDeadline(maxDelay)
                        .setExtras(bundle)
                        .build())
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun isJobIdRunning(JobId: Int): Boolean {
        jobScheduler?.allPendingJobs?.forEach { jobInfo ->
            if (jobInfo.id == JobId) {
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler?.cancelAll()
            applicationContext.stopService(Intent(applicationContext, NotificationChatJobService::class.java))
        }
        super.onDestroy()
    }

    private fun clearNotification(notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(notificationId)
    }

    private fun isEnableReplyChatNotification(): Boolean {
        return remoteConfig?.getBoolean(RemoteConfigKey.ENABLE_PUSH_NOTIFICATION_CHAT_SELLER, false) == true
    }
}