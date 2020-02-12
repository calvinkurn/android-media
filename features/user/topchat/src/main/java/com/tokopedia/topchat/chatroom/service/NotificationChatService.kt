package com.tokopedia.topchat.chatroom.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.core.app.RemoteInput
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatUseCase
import rx.Subscriber
import javax.inject.Inject
import androidx.core.app.NotificationManagerCompat
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class NotificationChatService: JobIntentService() {

    private val REPLY_KEY = "reply_chat_key"
    private val MESSAGE_ID = "message_chat_id"
    private val NOTIFICATION_ID = "notification_id"

    @Inject
    lateinit var replyChatUseCase: ReplyChatUseCase

    private var jobScheduler: JobScheduler? = null

    companion object {
        private const val JOB_ID_RETRY = 712
        private const val JOB_ID_NOTIFICATION = 812

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NotificationChatService::class.java, JOB_ID_NOTIFICATION, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                    ?: return
        }
    }

    override fun onHandleWork(intent: Intent) {
        executeReplyChat(intent)
    }

    private fun executeReplyChat(intent: Intent) {

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        val message = if(intent.getStringExtra(REPLY_KEY).isNullOrEmpty()) {
            remoteInput?.getCharSequence(REPLY_KEY).toString()
        } else {
            intent.getStringExtra(REPLY_KEY)
        }

        val messageId = intent.getStringExtra(MESSAGE_ID)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)

        val params = ReplyChatUseCase.generateParam(messageId, message)

        replyChatUseCase.execute(params, object : Subscriber<ReplyChatViewModel>() {
            override fun onNext(response: ReplyChatViewModel) {
                if (response.isSuccessReplyChat) {
                    clearNotification(notificationId)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        jobScheduler?.cancel(JOB_ID_RETRY)
                    }
                } else {
                    onError(IllegalStateException())
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRetryJob(messageId, message, notificationId)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRetryJob(messageId: String,
                            message: String,
                            notificationId: Int) {

        val bundle = PersistableBundle()
        bundle.putString(MESSAGE_ID, messageId)
        bundle.putString(REPLY_KEY, message)
        bundle.putInt(NOTIFICATION_ID, notificationId)
        val minDelay = TimeUnit.SECONDS.toMillis(5)
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

    private fun clearNotification(notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(notificationId)
    }

}