package com.tokopedia.topchat.chatroom.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NotificationChatJobService : JobService() {

    private val REPLY_KEY = "reply_chat_key"
    private val MESSAGE_ID = "message_chat_id"
    private val NOTIFICATION_ID = "notification_id"
    private val USER_ID = "user_id"

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        val intent = Intent(applicationContext, NotificationChatService::class.java)
        intent.putExtra(MESSAGE_ID, params?.extras?.getString(MESSAGE_ID))
        intent.putExtra(NOTIFICATION_ID, params?.extras?.getInt(NOTIFICATION_ID))
        intent.putExtra(REPLY_KEY, params?.extras?.getString(REPLY_KEY))
        intent.putExtra(USER_ID, params?.extras?.getString(USER_ID))

        NotificationChatService.enqueueWork(applicationContext, intent)
        jobFinished(params, false)

        return true
    }
}