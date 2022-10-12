package com.tokopedia.tokochat.data.repository

import android.content.Context
import android.util.Log
import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.analytics.ConversationsAnalyticsTracker
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.config.ConversationsConfig
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.logging.ConversationsLogger
import com.gojek.conversations.utils.ConversationsConstants
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokochat.R
import retrofit2.Retrofit
import javax.inject.Inject

class TokoChatRepository @Inject constructor(
    private val retrofit: Retrofit,
    @ApplicationContext private val context: Context,
    private val babbleCourier: BabbleCourierClient
): ConversationsLogger.ILog, ConversationsAnalyticsTracker {

    private var conversationRepository: ConversationsRepository? = null

    fun getConversationRepository(): ConversationsRepository {
        if (conversationRepository == null) {
            ConversationsRepository.init(
                context, retrofit, this, this,
                conversationsConfig = getConversationsConfig(),
                courierClient = babbleCourier
            )
            conversationRepository = ConversationsRepository.instance
        }
        return conversationRepository!!
    }

    private fun getConversationsConfig(): ConversationsConfig {
        return ConversationsConfig(
            enabledChannelTypes = listOf(ChannelType.GroupBooking),
            notificationIcon = 0,
            isSupportMetaServiceEnabled = false,
            isMessageRetryEnabled = true,
            contactsVerifyingBatchSize =
            ConversationsConstants.FEATURE_CONTACTS_SYNC_BATCH_SIZE_DEFAULT,
            isPerformanceTracingEnabled = false,
            imageLoader = null,
            isFetchLatestChannelEnabled = false,
            contactSyncRateLimit = 5
        )
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
