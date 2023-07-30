package com.tokopedia.inbox.universalinbox.stub.common

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
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

    override fun getAppType(): String = ""

    override fun getOwnerId(): String = clientId

    override fun getOwnerType(): String = ""

    override fun getStateObserver(): Observable<CourierState> {
        return Observable.just(courierState)
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
