package com.tokochat.tokochat_config_common.repository.courier

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.conversations.courier.retry.policy.ExponentialWithJitterRetryPolicy
import com.gojek.conversations.courier.retry.policy.RetryPolicy
import com.gojek.courier.CourierConnection
import com.gojek.courier.common.AppType
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.messageadapter.text.TextMessageAdapterFactory
import com.gojek.courier.streamadapter.rxjava.RxJavaStreamAdapterFactory
import com.tokochat.tokochat_config_common.remote_config.TokoChatCourierRemoteConfigImpl.Companion.SHOULD_TRACK_MESSAGE_RECEIVE_EVENT
import com.tokochat.tokochat_config_common.util.TokoChatCourierStateObservable
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

class TokoChatBabbleCourierImpl @Inject constructor(
    private val courierConnection: CourierConnection,
    private val tokoChatCourierStateObservable: TokoChatCourierStateObservable,
    private val remoteConfig: RemoteConfig,
    private val userSession: UserSessionInterface
) : BabbleCourierClient {

    private val courier = courierConnection.createCourier(
        listOf(RxJavaStreamAdapterFactory()),
        // String type converter from Byte Array to String
        // Object Converter in BabbleService param
        listOf(TextMessageAdapterFactory(), GsonMessageAdapterFactory())
    )

    override fun <T> create(service: Class<T>): T {
        return courier.create(service)
    }

    override fun getStateObserver(): Observable<CourierState> = tokoChatCourierStateObservable.observe()

    // Value from remote config for enable/disable courier chat
    // Always true for now (we use courier, not WebSocket)
    override fun isEnabled(chatProfileId: String?): Boolean {
        return true
    }

    override fun shouldTrackMessageReceiveEvent(): Boolean {
        return remoteConfig.getBoolean(SHOULD_TRACK_MESSAGE_RECEIVE_EVENT, false)
    }

    // We don't need this, not required
    override fun getProfileApiRetryPolicy(): RetryPolicy {
        return ExponentialWithJitterRetryPolicy()
    }

    override fun init(chatProfileId: String?) {
        courierConnection.init(SOURCE_APP_INIT)
    }

    override fun getUniqueClientId(profileId: String): String {
        val appType = AppType.Tokopedia
        return "${appType.value}/${appType.owner}/${userSession.userId}"
    }

    companion object {
        const val SOURCE_APP_INIT = "App Init"
    }
}
