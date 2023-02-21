package com.tokochat.tokochat_config_common.util

import com.gojek.conversations.courier.CourierState
import com.gojek.courier.CourierConnection
import com.gojek.courier.event.handler.BaseCourierEventHandler
import com.gojek.mqtt.event.MqttEvent
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject

class TokoChatCourierStateObservable @Inject constructor(
    @TokoChatQualifier courierConnection: CourierConnection
) {
    private val publishSubject = BehaviorSubject.create<CourierState>()

    init {
        courierConnection.addEventHandler(object : BaseCourierEventHandler() {
            override fun onEvent(mqttEvent: MqttEvent) {
                when (mqttEvent) {
                    is MqttEvent.MqttConnectSuccessEvent -> {
                        publishSubject.onNext(CourierState.CONNECTED)
                    }
                    is MqttEvent.MqttDisconnectStartEvent -> {
                        publishSubject.onNext(CourierState.DISCONNECTING)
                    }
                    is MqttEvent.MqttDisconnectCompleteEvent -> {
                        publishSubject.onNext(CourierState.DISCONNECTED)
                    }
                    is MqttEvent.MqttConnectFailureEvent -> {
                        publishSubject.onNext(CourierState.CONNECT_FAILED)
                    }
                    is MqttEvent.MqttConnectionLostEvent -> {
                        publishSubject.onNext(CourierState.CONNECT_FAILED)
                    }
                    else -> {}
                }
            }
        })
    }

    fun observe(): Observable<CourierState> = publishSubject.asObservable()
}
