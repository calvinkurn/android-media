package com.tokopedia.interceptors.refreshtoken

import android.content.Context
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.interceptors.GqlBaseApi
import com.tokopedia.interceptors.RetrofitClientHelper
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

/**
 * Created by Yoris on 29/10/21.
 */

class RefreshTokenGql {

    private fun randomChar(): String =
        UUID.randomUUID().toString().replace(DASH_SYMBOL, "").substring(TRIM_LENGTH_START_IDX, TRIM_LENGTH_LAST_IDX)

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

        val responseCall = RetrofitClientHelper.getRetrofit(context, userSession, networkRouter).create(
            GqlBaseApi::class.java
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

        private const val DASH_SYMBOL = "-"

        private const val TRIM_LENGTH_START_IDX = 0
        private const val TRIM_LENGTH_LAST_IDX = 4
    }
}