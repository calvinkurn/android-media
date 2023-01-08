package com.tokopedia.interceptors.forcelogout

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.interceptors.GqlBaseApi
import com.tokopedia.interceptors.RetrofitClientHelper
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSessionInterface

class ForceLogoutUseCase(
    val context: Context,
    val userSession: UserSessionInterface,
    val networkRouter: NetworkRouter
) {
    private fun createParams(userId: Int): Map<String, Int> {
        return mapOf(
            "user_id" to userId
        )
    }

    fun execute(): ForceLogoutResponse? {
        val params = createParams(userSession.userId.toIntOrZero())

        val gqlQueryInterface = ForceLogoutQuery()
        val requestBody = GraphqlRequest(gqlQueryInterface, ForceLogoutResponse::class.java, params)

        val responseCall = RetrofitClientHelper.getRetrofit(context, userSession, networkRouter).create(
            GqlBaseApi::class.java
        ).getResponse(listOf(requestBody))

        return try {
            val result = responseCall.execute()
            val forceLogoutResponse = mapResponse(result.body())
            if(forceLogoutResponse != null) {
                return forceLogoutResponse
            }
            return null
        } catch (e: Exception) {
            null
        }
    }

    private fun mapResponse(jsonArray: JsonArray?): ForceLogoutResponse? {
        return try {
            val obj = jsonArray?.get(0)?.asJsonObject
            val data = obj?.getAsJsonObject("data")
            return Gson().fromJson(data, ForceLogoutResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}