package com.tokopedia.chat_service.view.activity

import android.content.Context
import android.os.Bundle
import com.gojek.conversations.logging.ConversationsLogger
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.chat_service.R

class ChatServiceActivity : BaseActivity(), ConversationsLogger.ILog {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_service)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun d(tag: String, message: String) {

    }

    override fun e(tag: String, error: String, e: Throwable) {

    }

    override fun v(tag: String, message: String) {

    }

    override fun w(tag: String, message: String) {

    }
}