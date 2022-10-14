package com.tokopedia.tokochat.data.repository.courier

import android.content.Context
import android.util.Log
import com.gojek.chuckmqtt.external.MqttChuckConfig
import com.gojek.chuckmqtt.external.MqttChuckInterceptor
import com.gojek.courier.CourierConnection
import com.gojek.courier.analytic.tracker.EventTracker
import com.gojek.courier.config.CourierRemoteConfig
import com.gojek.courier.di.CourierComponent
import com.gojek.courier.di.UsernameProvider
import com.gojek.mqtt.client.MqttInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokochat.util.TokoChatCourierConnectionLifecycle
import com.tokopedia.user.session.BuildConfig
import com.tokopedia.user.session.UserSessionInterface
import retrofit2.Retrofit
import javax.inject.Inject

class TokoChatCourierClientProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson,
    private val retrofit: Retrofit,
    private val userSession: UserSessionInterface,
    private val courierRemoteConfig: CourierRemoteConfig
) {

    // TODO: Change the value after BE ready
    fun initializeCourierConnection(): CourierConnection {
        val params = CourierComponent.Params(
            context = context,
            gson = gson,
            applicationId = "com.gojek.app.staging",
            retrofit = retrofit,
            authenticationApiUrl = "courier/v1/token",
            usernameProvider = getUsernameProvider(),
            eventTracker = getEventTracker(),
            mqttInterceptors = getMqttInterceptors(),
            debuggingEnabled = BuildConfig.DEBUG,
            courierRemoteConfig = courierRemoteConfig,
            connectionLifecycle = TokoChatCourierConnectionLifecycle
        )
        return CourierComponent.getOrCreate(params).courierConnection()
    }

    private fun getUsernameProvider(): UsernameProvider {
        return object : UsernameProvider {
            override fun get(): String {
                // TODO: Change this to userSession.userId
                return "3306058"
            }
        }
    }

    private fun getEventTracker(): EventTracker {
        return object : EventTracker {
            override fun trackEvent(name: String, properties: Map<String, Any>) {}
        }
    }

    private fun getMqttInterceptors(): List<MqttInterceptor> {
        return if (BuildConfig.DEBUG) {
            listOf(
                MqttChuckInterceptor(context, MqttChuckConfig())
            )
        } else{
            listOf()
        }
    }
}
