package com.tokopedia.tokofood.stub.common.util

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.conversations.courier.retry.policy.RetryPolicy
import rx.Observable
import javax.inject.Inject

class BabbleCourierClientStub @Inject constructor() : BabbleCourierClient {

    private var courierState: CourierState = CourierState.CONNECTED
    private var clientId: String = "testClientId"
    private var chatProfileId: String? = null

    fun setCourierState(newState: CourierState) {
        courierState = newState
    }

    fun setClientId(newClientId: String) {
        clientId = newClientId
    }

    override fun <T> create(service: Class<T>): T {
        return service.newInstance()
    }

    override fun getProfileApiRetryPolicy(): RetryPolicy {
        return object : RetryPolicy {
            override fun getRetryMillis(): Long {
                return 0L
            }
            override fun reset() {}
        }
    }

    override fun getStateObserver(): Observable<CourierState> {
        return Observable.just(courierState)
    }

    override fun getUniqueClientId(profileId: String): String {
        return clientId
    }

    override fun init(chatProfileId: String?) {
        this.chatProfileId = chatProfileId
    }

    override fun isEnabled(chatProfileId: String?): Boolean {
        return false
    }

    override fun shouldTrackMessageReceiveEvent(): Boolean {
        return false
    }
}
