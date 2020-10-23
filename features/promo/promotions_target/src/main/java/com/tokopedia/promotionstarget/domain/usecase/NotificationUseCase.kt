package com.tokopedia.promotionstarget.domain.usecase

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tokopedia.promotionstarget.data.GratifParams
import com.tokopedia.promotionstarget.data.di.GRATIFF_NOTIFICATION
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.data.notification.GratifNotificationResponse
import com.tokopedia.promotionstarget.fake.FakeResponse
import kotlinx.coroutines.delay
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class NotificationUseCase @Inject constructor(@Named(GRATIFF_NOTIFICATION) val queryString: String,
                                              val gqlWrapper: GqlUseCaseWrapper,
                                              val sharedPreferences: SharedPreferences) {
    private val PARAMS = GratifParams

    suspend fun getResponse(map: HashMap<String, Any>): GratifNotificationResponse {
        val delayInSecs = sharedPreferences.getInt("get_notification_delay", 0)
        if (delayInSecs > 0)
            delay(delayInSecs * 1000L)
        return gqlWrapper.getResponse(GratifNotificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(notificationID: Int, notificationEntryType: Int, paymentID: Long): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[PARAMS.NOTIFICATION_ID] = notificationID
        variables[PARAMS.NOTIFICATION_ENTRY_TYPE] = notificationEntryType
        variables[PARAMS.PAYMENT_ID] = paymentID
        return variables
    }

    suspend fun getFakeResponse(map: HashMap<String, Any>): GratifNotificationResponse {
        val delayInSecs = sharedPreferences.getInt("get_notification_delay", 0)
        if (delayInSecs > 0)
            Thread.sleep(delayInSecs * 1000L)

        val gson = Gson()
        val response = FakeResponse.GRATIF_RESPONSE
        val json = JSONObject(response)
        return gson.fromJson(json.getJSONObject("data").toString(), GratifNotificationResponse::class.java)
    }
}