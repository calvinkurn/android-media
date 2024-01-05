package com.tokopedia.tokochat.config.repository.courier

import com.gojek.conversations.courier.BabbleCourierClient
import com.gojek.conversations.courier.CourierState
import com.gojek.courier.CourierConnection
import com.gojek.courier.common.AppType
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.messageadapter.text.TextMessageAdapterFactory
import com.gojek.courier.streamadapter.rxjava.RxJavaStreamAdapterFactory
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.config.remoteconfig.TokoChatCourierRemoteConfigImpl.Companion.SHOULD_TRACK_MESSAGE_RECEIVE_EVENT
import com.tokopedia.tokochat.config.util.TokoChatCourierStateObservable
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

class TokoChatBabbleCourierImpl @Inject constructor(
    private val courierConnection: CourierConnection,
    private val tokoChatCourierStateObservable: TokoChatCourierStateObservable,
    private val remoteConfig: RemoteConfig,
    private val userSession: UserSessionInterface
) : BabbleCourierClient {

    private val appType = AppType.Tokopedia

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

    override fun init(chatProfileId: String?) {
        if (!remoteConfig.getBoolean(COURIER_CONVERSATION_INIT)) {
            courierConnection.init(SOURCE_APP_INIT)
        }
    }

    override fun getAppType(): String = appType.value

    override fun getOwnerId(): String = userSession.userId

    override fun getOwnerType(): String = appType.owner

    companion object {
        const val SOURCE_APP_INIT = "App Init"
        const val COURIER_CONVERSATION_INIT = "android_courier_conversation_init"
    }
}
