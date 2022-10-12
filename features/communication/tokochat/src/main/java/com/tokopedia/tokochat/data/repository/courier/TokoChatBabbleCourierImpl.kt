package com.tokopedia.tokochat.data.repository.courier

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.conversations.courier.retry.policy.ExponentialWithJitterRetryPolicy
import com.gojek.conversations.courier.retry.policy.RetryPolicy
import com.gojek.courier.CourierConnection
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.streamadapter.rxjava.RxJavaStreamAdapterFactory
import com.tokopedia.tokochat.util.TokoChatCourierStateObservable
import rx.Observable
import javax.inject.Inject

class TokoChatBabbleCourierImpl @Inject constructor(
    private val courierConnection: CourierConnection,
    private val tokoChatCourierStateObservable: TokoChatCourierStateObservable
): BabbleCourierClient {

    private val courier = courierConnection.createCourier(
        listOf(RxJavaStreamAdapterFactory()),
        listOf(GsonMessageAdapterFactory())
    )

    override fun <T> create(service: Class<T>): T {
        return courier.create(service)
    }

    override fun getStateObserver(): Observable<CourierState> = tokoChatCourierStateObservable.observe()

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

    override fun init(chatProfileId: String?) {
        chatProfileId?.let {
            courierConnection.init("", it)
        }
    }

}
