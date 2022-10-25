package com.tokochat.tokochat_config_common.repository.courier

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.conversations.courier.retry.policy.ExponentialWithJitterRetryPolicy
import com.gojek.conversations.courier.retry.policy.RetryPolicy
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.streamadapter.rxjava.RxJavaStreamAdapterFactory
import com.tokochat.tokochat_config_common.util.TokoChatConnection
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokochat.tokochat_config_common.util.TokoChatCourierRemoteConfigImpl.Companion.SHOULD_TRACK_MESSAGE_RECEIVE_EVENT
import com.tokochat.tokochat_config_common.util.TokoChatCourierStateObservable
import rx.Observable
import javax.inject.Inject

class TokoChatBabbleCourierImpl @Inject constructor(
    private val tokoChatCourierStateObservable: TokoChatCourierStateObservable,
    private val remoteConfig: RemoteConfig
): BabbleCourierClient {

    private val courier = TokoChatConnection.courierConnection?.createCourier(
        listOf(RxJavaStreamAdapterFactory()),
        listOf(GsonMessageAdapterFactory())
    )

    override fun <T> create(service: Class<T>): T {
        return courier?.create(service) as T
    }

    override fun getStateObserver(): Observable<CourierState> = tokoChatCourierStateObservable.observe()

    //Value from remote config for enable/disable courier chat
    //Always true for now (we use courier, not WebSocket)
    override fun isEnabled(chatProfileId: String?): Boolean {
        return true
    }

    override fun shouldTrackMessageReceiveEvent(): Boolean {
        return remoteConfig.getBoolean(SHOULD_TRACK_MESSAGE_RECEIVE_EVENT, false)
    }

    //We don't need this, not required
    override fun getProfileApiRetryPolicy(): RetryPolicy {
        return ExponentialWithJitterRetryPolicy()
    }

    override fun init(chatProfileId: String?) {
        chatProfileId?.let {
            TokoChatConnection.courierConnection?.init(SOURCE_APP_INIT, it)
        }
    }

    companion object {
        const val SOURCE_APP_INIT = "App Init"
    }
}
