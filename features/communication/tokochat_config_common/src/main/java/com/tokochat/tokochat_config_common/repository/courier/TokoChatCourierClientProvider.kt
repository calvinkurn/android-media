package com.tokochat.tokochat_config_common.repository.courier

import android.content.Context
import com.gojek.chuckmqtt.external.MqttChuckConfig
import com.gojek.chuckmqtt.external.MqttChuckInterceptor
import com.gojek.courier.CourierConnection
import com.gojek.courier.analytic.tracker.EventTracker
import com.gojek.courier.common.AppType
import com.gojek.courier.config.CourierRemoteConfig
import com.gojek.courier.di.CourierComponent
import com.gojek.courier.di.UsernameProvider
import com.gojek.mqtt.client.MqttInterceptor
import com.google.gson.Gson
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.util.TokoChatCourierConnectionLifecycle
import com.tokopedia.config.BuildConfig
import com.tokopedia.user.session.UserSessionInterface
import retrofit2.Retrofit
import javax.inject.Inject

class TokoChatCourierClientProvider @Inject constructor(
    @TokoChatQualifier private val context: Context,
    private val gson: Gson,
    @TokoChatQualifier private val retrofit: Retrofit,
    private val userSession: UserSessionInterface,
    private val courierRemoteConfig: CourierRemoteConfig
) {

    fun getCourierConnection(): CourierConnection {
        val params = CourierComponent.Params(
            context = context,
            gson = gson,
            applicationId = context.packageName,
            retrofit = retrofit,
            authenticationApiUrl = AUTHENTICATION_END_POINT,
            usernameProvider = getUsernameProvider(),
            eventTracker = getEventTracker(),
            mqttInterceptors = getMqttInterceptors(),
            debuggingEnabled = BuildConfig.DEBUG,
            courierRemoteConfig = courierRemoteConfig,
            connectionLifecycle = TokoChatCourierConnectionLifecycle,
            appType = AppType.Tokopedia.INSTANCE
        )

        return CourierComponent.getOrCreate(params).courierConnection()
    }

    private fun getUsernameProvider(): UsernameProvider {
        return object : UsernameProvider {
            override fun get(): String {
                return userSession.userId
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
        } else {
            listOf()
        }
    }

    companion object {
        private const val AUTHENTICATION_END_POINT = "v1/token"
    }
}
