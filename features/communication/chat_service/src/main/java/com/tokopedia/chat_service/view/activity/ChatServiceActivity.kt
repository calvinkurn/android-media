package com.tokopedia.chat_service.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gojek.conversations.logging.ConversationsLogger
import com.tokopedia.chat_service.R

class ChatServiceActivity : AppCompatActivity(), ConversationsLogger.ILog {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_service)
    }

    private fun initConversationRepository() {

    }

    override fun d(tag: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun e(tag: String, error: String, e: Throwable) {
        TODO("Not yet implemented")
    }

    override fun v(tag: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun w(tag: String, message: String) {
        TODO("Not yet implemented")
    }
}