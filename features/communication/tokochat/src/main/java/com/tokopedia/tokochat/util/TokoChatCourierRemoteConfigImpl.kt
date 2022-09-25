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
        get() = remoteConfig.getString("", "100").toIntOrZero()

    /**
     * Ping interval to keep the mqtt alive
     * value is in seconds
     */
    override val courierKeepAliveInterval: Int
        get() = remoteConfig.getString("", "30").toIntOrZero()

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
        get() = remoteConfig.getBoolean("", false)

    /**
     * Retry configuration
     */
    override val connectRetryConfig: ConnectRetryTimeConfig
        get() = ConnectRetryTimeConfig()

    /**
     * Timeout configuration
     */
    override val connectTimeoutConfig: ConnectTimeoutConfig
        get() = ConnectTimeoutConfig()

    /**
     * Subscription Retry Configuration
     */
    override val subscriptionRetryConfig: SubscriptionRetryConfig
        get() = SubscriptionRetryConfig()

    /**
     * Connection idle time limit
     * If there's no action (or ping) after interval, the connection will timeout and disconnect
     * Need to bigger than courierKeepAliveInterval
     * Value in seconds
     */
    override val readTimeoutSeconds: Int
        get() = remoteConfig.getString("", "120").toIntOrZero()

    /**
     * Interval to reset connection config
     * After this interval time, the exponential retry (and other config if any) will be reset
     * Value in seconds
     */
    override val courierPolicyResetTime: Int
        get() = remoteConfig.getString("", "300").toIntOrZero()

    /**
     * Timeout for acknowledgement (server send back the response),
     * Should be lower than readTimeout
     * Value in seconds
     */
    override val courierInactivityTimeout: Int
        get() = remoteConfig.getString("", "60").toIntOrZero()

    /**
     * Interval for checking inactivity
     * Check if inactivity timeout happen or not in this interval
     * Should be lower than inactivity timeout
     * Value in seconds
     */
    override val courierActivityCheckInterval: Int
        get() = remoteConfig.getString("", "30").toIntOrZero()

    /**
     * Configuration lifecycle of connection
     * If set true, disconnect the connection when the app is in background
     */
    override val shouldDisconnectCourierOnBackground: Boolean
        get() = remoteConfig.getBoolean("", true)

    /**
     * Set the strategy on how to store the token
     * Token expiration is depend on the use cases
     * For example 1 hour or 6 hours
     */
    override val tokenCachingMechanism: TokenCachingMechanism
        get() = TokenCachingMechanism.DISK_BASED

    /**
     * Wait for this interval before disconnect
     * So there won't be any spam connect and disconnect
     * Related to shouldDisconnectCourierOnBackground for background connection
     * Value in seconds
     */
    override val disconnectDelaySeconds: Int
        get() = remoteConfig.getString("", "10").toIntOrZero()

    /**
     * Retry for background connection
     * Depend on shouldDisconnectCourierOnBackground
     */
    override val isAuthRetryWorkerEnabled: Boolean
        get() = false

    /**
     * Save the incoming message and wait before delete the message if there is no topic listener
     * Value in seconds
     */
    override val incomingMessagesTTL: Long
        get() = remoteConfig.getLong("", 86400)

    /**
     * Message clean up interval
     * Value in seconds
     */
    override val incomingMessagesCleanupInterval: Long
        get() = remoteConfig.getLong("", 60)

    /**
     * Logging purpose
     * App version, app state, OS version, & network
     */
    override val shouldLogUserProperties: Boolean
        get() = remoteConfig.getBoolean("", true)

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
        get() = false
    override val adaptiveKeepAliveLowerBound: Int? = null
    override val adaptiveKeepAliveUpperBound: Int? = null
    override val adaptiveKeepAliveStep: Int? = null
    override val optimalKeepAliveResetLimit: Int? = null
    override val adaptivePingSender: AdaptiveMqttPingSender? = null

    /**
     * Setup to enable the receiver when device idle
     * Only usable for background connection
     */
    override val isDeviceIdleModeReceiverEnabled: Boolean
        get() = false
}
