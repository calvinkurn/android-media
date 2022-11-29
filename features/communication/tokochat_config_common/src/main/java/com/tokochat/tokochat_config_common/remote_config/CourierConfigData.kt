package com.tokochat.tokochat_config_common.remote_config

import com.google.gson.annotations.SerializedName

data class CourierConfigData(
    @SerializedName("courierEventProbability")
    val courierEventProbability: Int = 0,

    @SerializedName("courierEventSamplingUpperBound")
    val courierEventSamplingUpperBound: Int = 100,

    @SerializedName("courierKeepAliveInterval")
    val courierKeepAliveInterval: Int = 30,

    @SerializedName("courierCleanSessionFlag")
    val courierCleanSessionFlag: Boolean = false,

    @SerializedName("maxRetryTimeConfig")
    val maxRetryTimeConfig: Int = 10,

    @SerializedName("reconnectTimeFixed")
    val reconnectTimeFixed: Int = 0,

    @SerializedName("reconnectTimeRandom")
    val reconnectTimeRandom: Int = 10,

    @SerializedName("maxReconnectTime")
    val maxReconnectTime: Int = 30,

    @SerializedName("readTimeoutSeconds")
    val readTimeoutSeconds: Int = 40,

    @SerializedName("sslHandshakeTimeOut")
    val sslHandshakeTimeOut: Int = 30,

    @SerializedName("sslUpperBoundConnTimeOut")
    val sslUpperBoundConnTimeOut: Int = 10,

    @SerializedName("upperBoundCountTimeOut")
    val upperBoundCountTimeOut: Int = 10,

    @SerializedName("maxRetryCount")
    val maxRetryCount: Int = 3,

    @SerializedName("courierPolicyResetTime")
    val courierPolicyResetTime: Int = 120,

    @SerializedName("courierInactivityTimeout")
    val courierInactivityTimeout: Int = 45,

    @SerializedName("courierActivityCheckInterval")
    val courierActivityCheckInterval: Int = 15,

    @SerializedName("tokenCachingMechanism")
    val tokenCachingMechanism: String = "disk",

    @SerializedName("disconnectDelaySeconds")
    val disconnectDelaySeconds: Int = 30,

    @SerializedName("isAuthRetryWorkerEnabled")
    val isAuthRetryWorkerEnabled: Boolean = false,

    @SerializedName("incomingMessagesTTL")
    val incomingMessagesTTL: Long = 86400,

    @SerializedName("incomingMessagesCleanupInterval")
    val incomingMessagesCleanupInterval: Long = 60,

    @SerializedName("shouldLogUserProperties")
    val shouldLogUserProperties: Boolean = false,

    @SerializedName("shouldUseAlpnProtocol")
    val shouldUseAlpnProtocol: Boolean = false,

    @SerializedName("isAdaptiveKeepAliveEnabled")
    val isAdaptiveKeepAliveEnabled: Boolean = false,

    @SerializedName("adaptiveKeepAliveLowerBound")
    val adaptiveKeepAliveLowerBound: Int? = null,

    @SerializedName("adaptiveKeepAliveUpperBound")
    val adaptiveKeepAliveUpperBound: Int? = null,

    @SerializedName("adaptiveKeepAliveStep")
    val adaptiveKeepAliveStep: Int? = null,

    @SerializedName("optimalKeepAliveResetLimit")
    val optimalKeepAliveResetLimit: Int? = null,

    @SerializedName("isDeviceIdleModeReceiverEnabled")
    val isDeviceIdleModeReceiverEnabled: Boolean = false,

    @SerializedName("messageEnvelopeEnabled")
    val isMessageEnvelopeEnabled: Boolean = true,

    @SerializedName("shouldUseNewSSLFlow")
    val shouldUseNewSSLFlow: Boolean = true
)
