package com.tokopedia.tokochat.config.repository

import android.content.Context
import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.analytics.ConversationsAnalyticsTracker
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.config.ConversationsConfig
import com.gojek.conversations.config.ConversationsGroupBookingConfig
import com.gojek.conversations.config.ConversationsGroupBookingNotificationConfig
import com.gojek.conversations.config.ConversationsGroupBookingNotificationConfig.NotificationHandler
import com.gojek.conversations.config.GroupBookingType
import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.logging.ConversationsLogger
import com.gojek.conversations.utils.ConversationsConstants
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.remoteconfig.TokoChatCourierRemoteConfigImpl.Companion.LOCAL_PUSH_NOTIFICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

open class TokoChatRepository @Inject constructor(
    @TokoChatQualifier private val retrofit: Retrofit,
    @TokoChatQualifier private val context: Context,
    @TokoChatQualifier private val babbleCourier: BabbleCourierClient,
    @TokoChatQualifier private val remoteConfig: RemoteConfig
) : ConversationsLogger.ILog, ConversationsAnalyticsTracker {

    private val job = Job()
    private val mainScope = CoroutineScope(job + Dispatchers.Main)

    @Synchronized
    fun getConversationRepository(): ConversationsRepository? {
        if (ConversationsRepository.instance == null) {
            initConversationRepository()
        }
        return ConversationsRepository.instance
    }

    /**
     * Conversation Repository initialisation needs to be in main thread
     * Because inside the init function in Gojek SDK, livedata setValue was called
     */
    fun initConversationRepository() {
        mainScope.launch {
            ConversationsRepository.init(
                context = context,
                retrofit = retrofit,
                logger = this@TokoChatRepository,
                analyticsTracker = this@TokoChatRepository,
                conversationsConfig = getConversationsConfig(),
                courierClient = babbleCourier,
                d2cConfig = getD2CConfig()
            )
        }
    }

    private fun getConversationsConfig(): ConversationsConfig {
        return ConversationsConfig(
            enabledChannelTypes = listOf(ChannelType.GroupBooking),
            notificationIcon = GlobalConfig.LAUNCHER_ICON_RES_ID,
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

    private fun getD2CConfig(): ConversationsGroupBookingConfig {
        return ConversationsGroupBookingConfig(
            type = GroupBookingType.CHANNEL_TYPE_GROUP_BOOKING,
            notificationConfig = ConversationsGroupBookingNotificationConfig(
                chatUiDeeplink = "", // Chat room applink
                ongoingOrdersListDeeplink = "", // Chat list applink
                notificationHandler = getNotificationHandler()
            )
        )
    }

    private fun getNotificationHandler(): NotificationHandler {
        return object : NotificationHandler {
            override fun shouldShowNotification(orderId: String, channel: String): Boolean {
                return remoteConfig.getBoolean(LOCAL_PUSH_NOTIFICATION, false)
            }
        }
    }

    override fun trackEvent(name: String, properties: Map<String, Any>) {}

    override fun trackPeopleProperty(propertyMap: Map<String, Any>) {}

    override fun d(tag: String, message: String) {
        Timber.d("$tag - $message")
    }

    override fun e(tag: String, error: String, e: Throwable) {
        Timber.e("$tag - $error")
    }

    override fun v(tag: String, message: String) {
        Timber.v("$tag - $message")
    }

    override fun w(tag: String, message: String) {
        Timber.w("$tag - $message")
    }
}
