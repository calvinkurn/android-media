package com.tokopedia.chat_service.data.repository

import android.content.Context
import android.util.Log
import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.analytics.ConversationsAnalyticsTracker
import com.gojek.conversations.logging.ConversationsLogger
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Inject

class ChatServiceRepository @Inject constructor(
    private val retrofit: Retrofit,
    @ApplicationContext private val context: Context
): ConversationsLogger.ILog, ConversationsAnalyticsTracker {

    private var conversationRepository: ConversationsRepository? = null

    fun getConversationRepository(): ConversationsRepository {
        if (conversationRepository == null) {
            ConversationsRepository.init(
                context, retrofit, this, this
            )
            conversationRepository = ConversationsRepository.instance
        }
        return conversationRepository!!
    }

    override fun trackEvent(name: String, properties: Map<String, Any>) {
    }

    override fun trackPeopleProperty(propertyMap: Map<String, Any>) {
    }

    override fun d(tag: String, message: String) {
        Log.d("ChatServiceRepository-d", message)
    }

    override fun e(tag: String, error: String, e: Throwable) {
        Log.d("ChatServiceRepository-e", error)
        Log.d("ChatServiceRepository-e", e.message.toString())
    }

    override fun v(tag: String, message: String) {
        Log.d("ChatServiceRepository-v", message)
    }

    override fun w(tag: String, message: String) {
        Log.d("ChatServiceRepository-w", message)
    }
}