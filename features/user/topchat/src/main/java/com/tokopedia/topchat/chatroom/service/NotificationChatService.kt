package com.tokopedia.topchat.chatroom.service

import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.core.app.RemoteInput
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatUseCase
import rx.Subscriber
import javax.inject.Inject
import androidx.core.app.NotificationManagerCompat


class NotificationChatService : JobIntentService() {

    private val REPLY_KEY = "reply_chat_key"
    private val MESSAGE_ID = "message_chat_id"
    private val NOTIFICATION_ID = "notification_id"

    @Inject
    lateinit var replyChatUseCase: ReplyChatUseCase

    override fun onCreate() {
        super.onCreate()
        DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        val messageId = intent.getStringExtra(MESSAGE_ID)
        val message = remoteInput?.getCharSequence(REPLY_KEY)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)

        val requestParam = ReplyChatUseCase.generateParam(messageId, message.toString())

        replyChatUseCase.execute(requestParam, object : Subscriber<ReplyChatViewModel>() {
            override fun onNext(response: ReplyChatViewModel?) {
                if (response != null) {
                    if (response.isSuccessReplyChat) {
                        clearNotification(notificationId)
                    }
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                clearNotification(notificationId)
            }
        })

    }

    private fun clearNotification(notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.cancel(notificationId)
    }

}