package com.tokopedia.tokochat.util

import com.gojek.courier.auth.cache.TokenCachingMechanism
import com.gojek.courier.config.CourierRemoteConfig
import com.gojek.mqtt.pingsender.AdaptiveMqttPingSender
import com.gojek.mqtt.pingsender.MqttPingSender
import com.gojek.mqtt.policies.connectretrytime.ConnectRetryTimeConfig
import com.gojek.mqtt.policies.connecttimeout.ConnectTimeoutConfig
import com.gojek.mqtt.policies.subscriptionretry.SubscriptionRetryConfig
import com.gojek.timer.pingsender.TimerPingSenderFactory
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class TokoChatCourierRemoteConfigImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
): CourierRemoteConfig {

    /**
     * How many events want to log, relate to tracker
     * can be 0 and the value is in percentage
     */
    override val courierEventProbability: Int
        get() = remoteConfig.getString(COURIER_EVENT_PROBABILITY, "0").toIntOrZero()

    /**
     * Ping interval to keep the mqtt alive
     * value is in seconds
     */
    override val courierKeepAliveInterval: Int
        get() = remoteConfig.getString(COURIER_KEEP_ALIVE_INTERVAL, "30").toIntOrZero()

    /**
     * On connection, a client sets the “clean session” flag.
     * If the clean session is set to false, means when the client disconnects,
     * any subscriptions it has will remain and any subsequent QoS 1 or 2 messages will be stored
     * until it connects again in the future.
     *
     * If the clean session is true,
     * then all subscriptions will be removed from the client when it disconnects.
     *
     * For default set to false
     */
    override val courierCleanSessionFlag: Boolean
        get() = remoteConfig.getBoolean(COURIER_CLEAN_SESSION_FLAG, false)

    /**
     * Retry configuration
     */
    override val connectRetryConfig: ConnectRetryTimeConfig
        get() {
            val maxRetryTimeConfig = remoteConfig.getString(MAX_RETRY_TIME_CONFIG, "10").toIntOrZero()
            val reconnectTimeFixed = remoteConfig.getString(RECONNECT_TIME_FIXED ,"0").toIntOrZero()
            val reconnectTimeRandom = remoteConfig.getString(RECONNECT_TIME_RANDOM, "10").toIntOrZero()
            val maxReconnectTime = remoteConfig.getString(MAX_RECONNECT_TIME, "30").toIntOrZero()
            return ConnectRetryTimeConfig(
                maxRetryTimeConfig,
                reconnectTimeFixed,
                reconnectTimeRandom,
                maxReconnectTime)
        }

    /**
     * Timeout configuration
     */
    override val connectTimeoutConfig: ConnectTimeoutConfig
        get() {
            val sslHandshakeTimeOut = remoteConfig.getString(SSL_HAND_SHAKE_TIMEOUT, "30").toIntOrZero()
            val sslUpperBoundConnTimeOut = remoteConfig.getString(SSL_UPPER_BOUND_CONN_TIMEOUT, "10").toIntOrZero()
            val upperBoundCountTimeOut = remoteConfig.getString(UPPER_BOUND_COUNT_TIMEOUT, "10").toIntOrZero()
            return ConnectTimeoutConfig(
                sslHandshakeTimeOut,
                sslUpperBoundConnTimeOut,
                upperBoundCountTimeOut)
        }

    /**
     * Subscription Retry Configuration
     */
    override val subscriptionRetryConfig: SubscriptionRetryConfig
        get() {
            val maxRetryCount = remoteConfig.getString(MAX_RETRY_COUNT, "3").toIntOrZero()
            return SubscriptionRetryConfig(maxRetryCount)
        }

    /**
     * Connection idle time limit
     * If there's no action (or ping) after interval, the connection will timeout and disconnect
     * Need to bigger than courierKeepAliveInterval
     * Value in seconds
     */
    override val readTimeoutSeconds: Int
        get() = remoteConfig.getString(READ_TIMEOUT_SECONDS, "40").toIntOrZero()

    /**
     * Interval to reset connection config
     * After this interval time, the exponential retry (and other config if any) will be reset
     * Value in seconds
     */
    override val courierPolicyResetTime: Int
        get() = remoteConfig.getString(COURIER_POLICY_RESET_TIME, "120").toIntOrZero()

    /**
     * Timeout for acknowledgement (server send back the response),
     * Should be lower than readTimeout
     * Value in seconds
     */
    override val courierInactivityTimeout: Int
        get() = remoteConfig.getString(COURIER_INACTIVITY_TIMEOUT, "45").toIntOrZero()

    /**
     * Interval for checking inactivity
     * Check if inactivity timeout happen or not in this interval
     * Should be lower than inactivity timeout
     * Value in seconds
     */
    override val courierActivityCheckInterval: Int
        get() = remoteConfig.getString(COURIER_ACTIVITY_CHECK_INTERVAL, "15").toIntOrZero()

    /**
     * Set the strategy on how to store the token
     * Token expiration is depend on the use cases
     * For example 1 hour or 6 hours
     */
    override val tokenCachingMechanism: TokenCachingMechanism
        get() {
            return when (remoteConfig.getString(TOKEN_CACHING_MECHANISM, "disk")) {
                "default" -> TokenCachingMechanism.NO_OP
                "memory" -> TokenCachingMechanism.IN_MEMORY
                "disk" -> TokenCachingMechanism.DISK_BASED
                else -> TokenCachingMechanism.DISK_BASED
            }
        }

    /**
     * Wait for this interval before disconnect
     * So there won't be any spam connect and disconnect
     * Related to shouldDisconnectCourierOnBackground for background connection
     * Value in seconds
     */
    override val disconnectDelaySeconds: Int
        get() = remoteConfig.getString(DISCONNECT_DELAY_SECONDS, "30").toIntOrZero()

    /**
     * Retry for background connection
     * Depend on shouldDisconnectCourierOnBackground
     */
    override val isAuthRetryWorkerEnabled: Boolean
        get() = remoteConfig.getBoolean(IS_AUTH_RETRY_WORKER_ENABLED, false)

    /**
     * Save the incoming message and wait before delete the message if there is no topic listener
     * Value in seconds
     */
    override val incomingMessagesTTL: Long
        get() = remoteConfig.getLong(INCOMING_MESSAGES_TTL, 86400)

    /**
     * Message clean up interval
     * Value in seconds
     */
    override val incomingMessagesCleanupInterval: Long
        get() = remoteConfig.getLong(INCOMING_MESSAGES_CLEANUP_INTERVAL, 60)

    /**
     * Logging purpose
     * App version, app state, OS version, & network
     */
    override val shouldLogUserProperties: Boolean
        get() = remoteConfig.getBoolean(SHOULD_LOG_USER_PROPERTIES, false)

    /**
     * Timer ping sender is only for  foreground
     * If background connection active please use alarm ping sender or work manager ping sender
     */
    override val pingSender: MqttPingSender
        get() = TimerPingSenderFactory.create()

    /**
     * Adaptive keep alive is based on network,
     * It finds the optimal time to keep the connection alive for background connection
     */
    override val isAdaptiveKeepAliveEnabled: Boolean
        get() = remoteConfig.getBoolean(IS_ADAPTIVE_KEEP_ALIVE_ENABLED, false)

    override val adaptiveKeepAliveLowerBound: Int?
        get() = getIntOrNull(ADAPTIVE_KEEP_ALIVE_LOWER_BOUND)

    override val adaptiveKeepAliveUpperBound: Int?
        get() = getIntOrNull(ADAPTIVE_KEEP_ALIVE_UPPER_BOUND)

    override val adaptiveKeepAliveStep: Int?
        get() = getIntOrNull(ADAPTIVE_KEEP_ALIVE_STEP)

    override val optimalKeepAliveResetLimit: Int?
        get() = getIntOrNull(OPTIMAL_KEEP_ALIVE_RESET_LIMIT)

    override val adaptivePingSender: AdaptiveMqttPingSender? = null

    private fun getIntOrNull(key: String): Int? {
        val value = remoteConfig.getString(key, "")
        return if (value.isNotEmpty()) {
            value.toIntOrZero()
        } else {
            null
        }
    }

    /**
     * Setup to enable the receiver when device idle
     * Only usable for background connection
     */
    override val isDeviceIdleModeReceiverEnabled: Boolean
        get() = remoteConfig.getBoolean(IS_DEVICE_IDLE_MODE_RECEIVER_ENABLED, false)

    companion object {
        private const val COURIER_EVENT_PROBABILITY = "android_tokochat_courierEventProbability"
        private const val COURIER_KEEP_ALIVE_INTERVAL = "android_tokochat_courierKeepAliveInterval"
        private const val COURIER_CLEAN_SESSION_FLAG = "android_tokochat_courierCleanSessionFlag"
        private const val READ_TIMEOUT_SECONDS = "android_tokochat_readTimeoutSeconds"
        private const val COURIER_POLICY_RESET_TIME = "android_tokochat_courierPolicyResetTime"
        private const val COURIER_INACTIVITY_TIMEOUT = "android_tokochat_courierInactivityTimeout"
        private const val COURIER_ACTIVITY_CHECK_INTERVAL = "android_tokochat_courierActivityCheckInterval"
        private const val TOKEN_CACHING_MECHANISM = "android_tokochat_tokenCachingMechanism"
        private const val DISCONNECT_DELAY_SECONDS = "android_tokochat_disconnectDelaySeconds"
        private const val IS_AUTH_RETRY_WORKER_ENABLED = "android_tokochat_isAuthRetryWorkerEnabled"
        private const val INCOMING_MESSAGES_TTL = "android_tokochat_incomingMessagesTTL"
        private const val INCOMING_MESSAGES_CLEANUP_INTERVAL = "android_tokochat_incomingMessagesCleanupInterval"
        private const val SHOULD_LOG_USER_PROPERTIES = "android_tokochat_shouldLogUserProperties"
        private const val IS_ADAPTIVE_KEEP_ALIVE_ENABLED = "android_tokochat_isAdaptiveKeepAliveEnabled"
        private const val ADAPTIVE_KEEP_ALIVE_LOWER_BOUND = "android_tokochat_adaptiveKeepAliveLowerBound"
        private const val ADAPTIVE_KEEP_ALIVE_UPPER_BOUND = "android_tokochat_adaptiveKeepAliveUpperBound"
        private const val ADAPTIVE_KEEP_ALIVE_STEP = "android_tokochat_adaptiveKeepAliveStep"
        private const val OPTIMAL_KEEP_ALIVE_RESET_LIMIT = "android_tokochat_optimalKeepAliveResetLimit"
        private const val IS_DEVICE_IDLE_MODE_RECEIVER_ENABLED = "android_tokochat_isDeviceIdleModeReceiverEnabled"
        private const val MAX_RETRY_TIME_CONFIG = "android_tokochat_maxRetryTimeConfig"
        private const val RECONNECT_TIME_FIXED = "android_tokochat_reconnectTimeFixed"
        private const val RECONNECT_TIME_RANDOM = "android_tokochat_reconnectTimeRandom"
        private const val MAX_RECONNECT_TIME = "android_tokochat_maxReconnectTime"
        private const val SSL_HAND_SHAKE_TIMEOUT = "android_tokochat_sslHandshakeTimeOut"
        private const val SSL_UPPER_BOUND_CONN_TIMEOUT = "android_tokochat_sslUpperBoundConnTimeOut"
        private const val UPPER_BOUND_COUNT_TIMEOUT = "android_tokochat_upperBoundCountTimeOut"
        private const val MAX_RETRY_COUNT = "android_tokochat_maxRetryCount"

        const val SHOULD_TRACK_MESSAGE_RECEIVE_EVENT = "android_tokochat_shouldTrackMessageReceiveEvent"
    }
}
