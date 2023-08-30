package com.tokopedia.tokochat.config.remoteconfig

import com.gojek.courier.auth.cache.TokenCachingMechanism
import com.gojek.courier.config.CourierRemoteConfig
import com.gojek.mqtt.pingsender.AdaptiveMqttPingSender
import com.gojek.mqtt.pingsender.MqttPingSender
import com.gojek.mqtt.policies.connectretrytime.ConnectRetryTimeConfig
import com.gojek.mqtt.policies.connecttimeout.ConnectTimeoutConfig
import com.gojek.mqtt.policies.subscriptionretry.SubscriptionRetryConfig
import com.gojek.timer.pingsender.TimerPingSenderFactory
import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class TokoChatCourierRemoteConfigImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
) : CourierRemoteConfig {

    private var courierConfigData = CourierConfigData()

    init {
        initCourierRemoteConfigData()
    }

    private fun initCourierRemoteConfigData() {
        val data = remoteConfig.getString(COURIER_CONFIG_JSON, "")
        try {
            if (data.isNotBlank()) {
                courierConfigData = Gson().fromJson(data, CourierConfigData::class.java)
            }
        } catch (throwable: Throwable) {
            ServerLogger.log(Priority.P2, ERROR_TAG, createErrorMessage(data, throwable))
        }
    }

    private fun createErrorMessage(
        data: String,
        throwable: Throwable
    ): Map<String, String> {
        return mutableMapOf(
            DATA_KEY to data,
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }

    /**
     * How many events want to log, relate to tracker
     * can be 0 and the value is in percentage
     */
    override val courierEventProbability: Int
        get() = courierConfigData.courierEventProbability

    /**
     * Should not be 0
     */
    override val courierEventSamplingUpperBound: Int
        get() = courierConfigData.courierEventSamplingUpperBound

    /**
     * Ping interval to keep the mqtt alive
     * value is in seconds
     */
    override val courierKeepAliveInterval: Int
        get() = courierConfigData.courierKeepAliveInterval

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
        get() = courierConfigData.courierCleanSessionFlag

    /**
     * Retry configuration
     */
    override val connectRetryConfig: ConnectRetryTimeConfig
        get() {
            val maxRetryTimeConfig = courierConfigData.maxRetryTimeConfig
            val reconnectTimeFixed = courierConfigData.reconnectTimeFixed
            val reconnectTimeRandom = courierConfigData.reconnectTimeRandom
            val maxReconnectTime = courierConfigData.maxReconnectTime
            return ConnectRetryTimeConfig(
                maxRetryTimeConfig,
                reconnectTimeFixed,
                reconnectTimeRandom,
                maxReconnectTime
            )
        }

    /**
     * Timeout configuration
     */
    override val connectTimeoutConfig: ConnectTimeoutConfig
        get() {
            val sslHandshakeTimeOut = courierConfigData.sslHandshakeTimeOut
            val sslUpperBoundConnTimeOut = courierConfigData.sslUpperBoundConnTimeOut
            val upperBoundCountTimeOut = courierConfigData.upperBoundCountTimeOut
            return ConnectTimeoutConfig(
                sslHandshakeTimeOut,
                sslUpperBoundConnTimeOut,
                upperBoundCountTimeOut
            )
        }

    /**
     * Subscription Retry Configuration
     */
    override val subscriptionRetryConfig: SubscriptionRetryConfig
        get() {
            val maxRetryCount = courierConfigData.maxRetryCount
            return SubscriptionRetryConfig(maxRetryCount)
        }

    /**
     * Connection idle time limit
     * If there's no action (or ping) after interval, the connection will timeout and disconnect
     * Need to bigger than courierKeepAliveInterval
     * Value in seconds
     */
    override val readTimeoutSeconds: Int
        get() = courierConfigData.readTimeoutSeconds

    /**
     * Interval to reset connection config
     * After this interval time, the exponential retry (and other config if any) will be reset
     * Value in seconds
     */
    override val courierPolicyResetTime: Int
        get() = courierConfigData.courierPolicyResetTime

    /**
     * Timeout for acknowledgement (server send back the response),
     * Should be lower than readTimeout
     * Value in seconds
     */
    override val courierInactivityTimeout: Int
        get() = courierConfigData.courierInactivityTimeout

    /**
     * Interval for checking inactivity
     * Check if inactivity timeout happen or not in this interval
     * Should be lower than inactivity timeout
     * Value in seconds
     */
    override val courierActivityCheckInterval: Int
        get() = courierConfigData.courierActivityCheckInterval

    /**
     * Set the strategy on how to store the token
     * Token expiration is depend on the use cases
     * For example 1 hour or 6 hours
     */
    override val tokenCachingMechanism: TokenCachingMechanism
        get() {
            return when (courierConfigData.tokenCachingMechanism) {
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
        get() = courierConfigData.disconnectDelaySeconds

    /**
     * Retry for background connection
     * Depend on shouldDisconnectCourierOnBackground
     */
    override val isAuthRetryWorkerEnabled: Boolean
        get() = courierConfigData.isAuthRetryWorkerEnabled

    /**
     * Save the incoming message and wait before delete the message if there is no topic listener
     * Value in seconds
     */
    override val incomingMessagesTTL: Long
        get() = courierConfigData.incomingMessagesTTL

    /**
     * Message clean up interval
     * Value in seconds
     */
    override val incomingMessagesCleanupInterval: Long
        get() = courierConfigData.incomingMessagesCleanupInterval

    /**
     * Logging purpose
     * App version, app state, OS version, & network
     */
    override val shouldLogUserProperties: Boolean
        get() = courierConfigData.shouldLogUserProperties

    /**
     * Enabling Application-Layer Protocol Negotiation (ALPN) Protocol
     * Transport Layer Security (TLS) extension that allows the application layer to negotiate
     * which protocol should be performed over a secure connection
     * in a manner that avoids additional round trips and which is independent of
     * the application-layer protocols.
     */
    override val shouldUseAlpnProtocol: Boolean
        get() = courierConfigData.shouldUseAlpnProtocol

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
        get() = courierConfigData.isAdaptiveKeepAliveEnabled

    override val adaptiveKeepAliveLowerBound: Int?
        get() = courierConfigData.adaptiveKeepAliveLowerBound

    override val adaptiveKeepAliveUpperBound: Int?
        get() = courierConfigData.adaptiveKeepAliveLowerBound

    override val adaptiveKeepAliveStep: Int?
        get() = courierConfigData.adaptiveKeepAliveStep

    override val optimalKeepAliveResetLimit: Int?
        get() = courierConfigData.optimalKeepAliveResetLimit

    override val adaptivePingSender: AdaptiveMqttPingSender? = null

    /**
     * Setup to enable the receiver when device idle
     * Only usable for background connection
     */
    override val isDeviceIdleModeReceiverEnabled: Boolean
        get() = courierConfigData.isDeviceIdleModeReceiverEnabled

    /**
     * Enable / Disable Envelope Messages (Represents Byte Array in MQTT Message Courier)
     * The usage is for end-to-end tracking purposes
     */
    override val isMessageEnvelopeEnabled: Boolean
        get() = courierConfigData.isMessageEnvelopeEnabled

    override val shouldUseNewSSLFlow: Boolean
        get() = courierConfigData.shouldUseNewSSLFlow

    companion object {
        private const val COURIER_CONFIG_JSON = "android_courier_config_json"
        private const val ERROR_TAG = "COURIER_CONNECTION_CONFIG"
        private const val DATA_KEY = "data"
        private const val STACKTRACE_KEY = "stacktrace"

        const val SHOULD_TRACK_MESSAGE_RECEIVE_EVENT = "android_tokochat_shouldTrackMessageReceiveEvent"
        const val LOCAL_PUSH_NOTIFICATION = "android_tokochat_local_push_notif_enabled"
    }
}
