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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by Yoris on 29/10/21.
 */

class RefreshTokenGql {

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
            ).build()
    }

    private fun randomChar(): String =
        UUID.randomUUID().toString().replace("-", "").substring(0, 4)

    private fun encode(type: String): String {
        return if (type.isNotBlank()) {
            val secretId = randomChar()
            val asB64 = Base64.encodeToString(type.toByteArray(), Base64.NO_WRAP)
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

    fun createBasicTokenGQL(): String {
        val clientID = "7ea919182ff"
        val asB64 = Base64.encodeToString(clientID.toByteArray(), Base64.NO_WRAP)
        val secretId = randomChar()
        return "$asB64$secretId"
    }

    fun refreshToken(context: Context, userSession: UserSessionInterface, networkRouter: NetworkRouter): RefreshTokenData? {
        userSession.setToken(createBasicTokenGQL(), "")

        val currentRefreshToken = EncoderDecoder.Decrypt(userSession.freshToken, userSession.refreshTokenIV)
        val params = createParams(currentRefreshToken, userSession.accessToken)
        val requestBody = GraphqlRequest(graphqlQuery(), RefreshTokenResponse::class.java, params)

        val responseCall = getRetrofit(context, userSession, networkRouter).create(
            RefreshTokenApi::class.java
        ).getResponse(listOf(requestBody))

        responseCall.request().headers("")
        return try {
            val result = responseCall.execute()
            val resultToken = mapRefreshTokenResponse(result.body())
            resultToken?.run {
                if(accessToken?.isNotEmpty() == true) {
                    userSession.setToken(accessToken, tokenType)
                }
                if(refreshToken?.isNotEmpty() == true) {
                    userSession.setRefreshToken(
                        EncoderDecoder.Encrypt(refreshToken, userSession.refreshTokenIV)
                    )
                }
            }
            resultToken
        } catch (e: Exception) {
            null
        }
    }

    private fun mapRefreshTokenResponse(jsonArray: JsonArray?): RefreshTokenData? {
        return try {
            val obj = jsonArray?.get(0)?.asJsonObject
            val data = obj?.getAsJsonObject("data")
            val resp = Gson().fromJson(data, RefreshTokenResponse::class.java)
            return resp.loginToken
       }catch (e: Exception) {
           null
       }
    }

    private fun graphqlQuery(): String = """
        mutation refresh_token(${'$'}grant_type: String!, ${'$'}refresh_token: String!, ${'$'}access_token: String!){
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