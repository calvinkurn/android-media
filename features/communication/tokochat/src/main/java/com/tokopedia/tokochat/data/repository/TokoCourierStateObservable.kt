package com.tokopedia.tokochat.data.repository

import com.gojek.conversations.courier.CourierState
import com.gojek.courier.CourierConnection
import com.gojek.courier.event.handler.BaseCourierEventHandler
import com.gojek.mqtt.event.MqttEvent
import rx.subjects.BehaviorSubject
import javax.inject.Inject

class TokoCourierStateObservable @Inject constructor(
    courierConnection: CourierConnection
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
                }
            }
        })
    }

    fun observe() = publishSubject.asObservable()
}
