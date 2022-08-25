package com.tokopedia.chat_service.data.repository

import android.content.Context
import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.analytics.ConversationsAnalyticsTracker
import com.gojek.conversations.logging.ConversationsLogger
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Inject

class ChatServiceRepository @Inject constructor(
    retrofit: Retrofit,
    @ApplicationContext context: Context
): ConversationsLogger.ILog, ConversationsAnalyticsTracker {

    val conversationRepository = ConversationsRepository.instance

    init {
        ConversationsRepository.init(
            context, retrofit, this, this
        )
    }

    override fun trackEvent(name: String, properties: Map<String, Any>) {
    }

    override fun trackPeopleProperty(propertyMap: Map<String, Any>) {
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