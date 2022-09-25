package com.tokopedia.tokochat.data.repository

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.conversations.courier.retry.policy.ExponentialWithJitterRetryPolicy
import com.gojek.conversations.courier.retry.policy.RetryPolicy
import com.gojek.courier.CourierConnection
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.streamadapter.rxjava.RxJavaStreamAdapterFactory
import rx.Observable
import javax.inject.Inject

class TokoChatBabbleCourierImpl @Inject constructor(
    courierConnection: CourierConnection,
    private val tokoCourierStateObservable: TokoCourierStateObservable
): BabbleCourierClient {

    private val courier = courierConnection.createCourier(
        listOf(RxJavaStreamAdapterFactory()),
        listOf(GsonMessageAdapterFactory())
    )

    override fun <T> create(service: Class<T>): T {
        return courier.create(service)
    }

    override fun getStateObserver(): Observable<CourierState> = tokoCourierStateObservable.observe()

    //Value from remote config for enable/disable courier chat
    //Always true for now (we use courier, not WebSocket)
    override fun isEnabled(chatProfileId: String?): Boolean {
        return true
    }

    //Remote config if we want to log or not
    override fun shouldTrackMessageReceiveEvent(): Boolean {
        return false
    }

    //We don't need this, not required
    override fun getProfileApiRetryPolicy(): RetryPolicy {
        return ExponentialWithJitterRetryPolicy()
    }

    //We don't need this, not required
    override fun init(chatProfileId: String?) {}

}
