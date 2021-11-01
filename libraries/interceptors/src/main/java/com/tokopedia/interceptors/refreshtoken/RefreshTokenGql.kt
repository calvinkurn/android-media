package com.tokopedia.interceptors.refreshtoken

import android.content.Context
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.akamai_bot_lib.interceptor.GqlAkamaiBotInterceptor
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by Yoris on 29/10/21.
 */

class RefreshTokenGql() {

    private fun getRetrofit(
        context: Context,
        userSession: UserSessionInterface,
        networkRouter: NetworkRouter
    ): Retrofit {

        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())

        return Retrofit.Builder()
            .baseUrl(GraphqlUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                tkpdOkHttpBuilder
                    .addInterceptor(TkpdAuthInterceptor(context, networkRouter, userSession))
                    .addInterceptor(FingerprintInterceptor(networkRouter, userSession))
                    .addInterceptor(GqlAkamaiBotInterceptor())
                    .build()
            )
            .build()
    }

    private fun randomChar(length: Int): String {
        return if (length > 0) {
            UUID.randomUUID().toString().replace("-", "").substring(0, length)
        } else {
            ""
        }
    }

    private fun encode(type: String): String {
        return if (type.isNotBlank()) {
            val secretId = randomChar(4)
            System.out.println("type : $type")
            val asB64 = Base64.encodeToString(type.toByteArray(), Base64.NO_WRAP)
            System.out.println("asB64 : $asB64")
            "$asB64$secretId"
        } else {
            ""
        }
    }

    private fun createParams(refreshToken: String, accessToken: String): Map<String, String> {
        return mapOf(
            GRANT_TYPE to encode("refresh_token"),
            REFRESH_TOKEN to encode(refreshToken),
            ACCESS_TOKEN to accessToken
        )
    }

    fun refreshToken(context: Context, userSession: UserSessionInterface, networkRouter: NetworkRouter): RefreshTokenData? {
        val currentRefreshToken = EncoderDecoder.Decrypt(userSession.freshToken, userSession.refreshTokenIV)
        val params = createParams("abcasdasd", "asdasd")
        val requestBody = GraphqlRequest(graphqlQuery(), RefreshTokenGqlResponse::class.java, params)

        val responseCall = getRetrofit(context, userSession, networkRouter).create(
            RefreshTokenApi::class.java
        ).getResponse(listOf(requestBody))

        try {
            val result = responseCall.execute()
            if(result.isSuccessful) {
                val element = result.body()?.get(0)
                val data = Gson().fromJson(element, RefreshTokenGqlResponse::class.java)
                return data.loginToken
            } else {
                println("result: ${result.isSuccessful} ${result.errorBody()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun refreshToken2(context: Context, userSession: UserSessionInterface, networkRouter: NetworkRouter): RefreshTokenData? {
        val currentRefreshToken = EncoderDecoder.Decrypt(userSession.freshToken, userSession.refreshTokenIV)
        val params = createParams(currentRefreshToken, userSession.accessToken)
        val requestBody = GraphqlRequest(graphqlQuery(), RefreshTokenGqlResponse::class.java, params)

        val responseCall = getRetrofit(context, userSession, networkRouter).create(
            RefreshTokenApi::class.java
        ).getResponse(listOf(requestBody))

        try {
            responseCall.enqueue(object: Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val result = mapRefreshTokenResponse(jsonArray = response.body())
                    if(response.isSuccessful) {
                        println("tokenbro: ${result.accessToken}")
                    } else {
                        println("result: ${response.isSuccessful} ${response.errorBody()}")
                    }
                    println("resultbro: $result")

                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    t.printStackTrace()
                    println("result:${t.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun mapRefreshTokenResponse(jsonArray: JsonArray?): RefreshTokenData {
        return try {
           val element = jsonArray?.get(0)
           val data = Gson().fromJson(element, RefreshTokenGqlResponse::class.java)
           data.loginToken
       }catch (e: Exception) {
           e.printStackTrace()
           RefreshTokenData()
       }
    }

    fun graphqlQuery(): String = """
        mutation refresh_token(${'$'}grant_type: String!, ${'$'}refresh_token: String!,  ${'$'}access_token: String!){
            login_token(
                input: {
                    grant_type: ${'$'}grant_type
                    refresh_token: ${'$'}refresh_token
                    access_token: ${'$'}access_token
                }
            ) {
                acc_sid
                access_token
                expires_in
                refresh_token
                sid
                token_type
                sq_check
                action
                errors {
                    name
                    message
                }
                event_code
            }
        }
    """.trimIndent()

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val GRANT_TYPE = "grant_type"
        private const val REFRESH_TOKEN = "refresh_token"

    }
}