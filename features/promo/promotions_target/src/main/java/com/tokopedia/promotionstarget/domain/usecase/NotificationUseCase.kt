package com.tokopedia.promotionstarget.domain.usecase

import com.google.gson.Gson
import com.tokopedia.promotionstarget.data.AutoApplyParams
import com.tokopedia.promotionstarget.data.di.GRATIFF_NOTIFICATION
import com.tokopedia.promotionstarget.data.gql.GqlUseCaseWrapper
import com.tokopedia.promotionstarget.data.notification.GratifNotificationResponse
import com.tokopedia.promotionstarget.fake.FakeResponse
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class NotificationUseCase @Inject constructor(@Named(GRATIFF_NOTIFICATION) val queryString: String, val gqlWrapper: GqlUseCaseWrapper) {
    private val PARAMS = AutoApplyParams

    suspend fun getResponse(map: HashMap<String, Any>): GratifNotificationResponse {
        return gqlWrapper.getResponse(GratifNotificationResponse::class.java, queryString, map)
    }

    fun getQueryParams(notificationID: Int, notificationEntryType: Int, paymentID: Int): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        return variables
    }

    suspend fun getFakeResponse(map: HashMap<String, Any>): GratifNotificationResponse {
        val gson = Gson()
        val response = FakeResponse.GRATIF_RESPONSE
        val json = JSONObject(response)
        return gson.fromJson(json.getJSONObject("data").toString(), GratifNotificationResponse::class.java)
    }
}