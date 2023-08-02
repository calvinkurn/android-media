package com.tokopedia.profilecompletion.domain

import android.util.Base64
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.di.ProfileManagementApi
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.time.TimeHelper
import com.tokopedia.webview.ext.encode
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class GetUrlProfileManagementUseCase@Inject constructor(
    private val profileManagementApi: ProfileManagementApi,
    private val userSessionInterface: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetUrlProfileManagementResult>(dispatchers.io) {
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Unit): GetUrlProfileManagementResult {
        val response = profileManagementApi.getSecret(MODULE_NAME_SEAMLESS).body()

        val currentTimeStamp = TimeHelper.getNowTimeStamp()
        val timeRfc3339 = TimeHelper.format(currentTimeStamp, FORMAT_RFC_3339)

        val content = createContent(
            token = response?.data?.token.toString(),
            userId = userSessionInterface.userId,
            date = timeRfc3339
        )

        val hmacSignature = hmacDigest(content, response?.data?.secretKey.toString())

        val url = createUrl(
            token = response?.data?.token.toString(),
            userId = userSessionInterface.userId,
            date = timeRfc3339,
            keyId = response?.data?.secretKeyId.toString(),
            hmacSignature = replaceHmacSignature(hmacSignature)
        )
        return GetUrlProfileManagementResult.Success(url)
    }

    private fun createContent(
        token: String,
        userId: String,
        clientId: String = VALUE_CLIENT_ID,
        url: String = TokopediaUrl.getInstance().GOTO_ACCOUNTS + VALUE_URL,
        backUrl: String = VALUE_BACK_URL,
        appLanguage: String = VALUE_LANGUAGE_IND,
        date: String
    ): String {
        return "$KEY_X_TOKEN=$token" + "\n" +
            "$KEY_X_USER_ID=$userId" + "\n" +
            "$KEY_X_CLIENT_ID=$clientId" + "\n" +
            "$KEY_X_URL=$url" + "\n" +
            "$KEY_X_BACK_URL=$backUrl" + "\n" +
            "$KEY_X_LANGUAGE=$appLanguage"+ "\n" +
            "$KEY_X_DATE=$date"
    }

    private fun createUrl(
        token: String,
        userId: String,
        clientId: String = VALUE_CLIENT_ID,
        url: String = TokopediaUrl.getInstance().GOTO_ACCOUNTS + VALUE_URL,
        backUrl: String = VALUE_BACK_URL,
        appLanguage: String = VALUE_LANGUAGE_IND,
        date: String,
        keyId: String,
        hmacSignature: String
    ): String {
        val parameter = (
                "$KEY_X_USER_ID=$userId" + "&" +
                "$KEY_X_URL=$url" + "&" +
                "$KEY_X_BACK_URL=$backUrl" + "&" +
                "$KEY_X_LANGUAGE=$appLanguage" + "&" +
                "$KEY_X_CLIENT_ID=$clientId" + "&" +
                "$KEY_X_AUTHORIZATION=$keyId" + ":" + hmacSignature + "&" +
                "$KEY_X_DATE=${date.encode()}" + "&" +
                "$KEY_X_TOKEN=$token"
            )

        return TokopediaUrl.getInstance().GOTO_ACCOUNTS + "$URL_PATH_SEAMLESS?$parameter"
    }

    private fun replaceHmacSignature(signature: String): String {
        return signature.replace("/", "_").replace("+", "-")
    }

    private fun hmacDigest(
        msg: String,
        key: String,
        alg: String = HMAC_ALGORITHM
    ): String {
        val signingKey = SecretKeySpec(key.toByteArray(), alg)
        val mac = Mac.getInstance(alg)
        mac.init(signingKey)

        val bytes = mac.doFinal(msg.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    companion object {
        private const val HMAC_ALGORITHM = "HmacSHA256"
        private const val FORMAT_RFC_3339 = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
        private const val MODULE_NAME_SEAMLESS = "seamless"
        private const val KEY_X_USER_ID = "x-user_id"
        private const val KEY_X_URL = "x-url"
        private const val KEY_X_BACK_URL = "x-back_url"
        private const val KEY_X_LANGUAGE = "x-app_language"
        private const val KEY_X_CLIENT_ID = "x-client_id"
        private const val KEY_X_AUTHORIZATION = "x-authorization"
        private const val KEY_X_DATE = "x-date"
        private const val KEY_X_TOKEN = "x-token"
        private const val VALUE_LANGUAGE_IND = "id"
        private const val VALUE_BACK_URL = "tokopedia://back"
        private const val VALUE_CLIENT_ID = "tokopedia:consumer:app"
        private const val VALUE_URL = "/profile/web/"
        private const val URL_PATH_SEAMLESS = "/goto-auth/seamless"
    }

}


sealed class GetUrlProfileManagementResult {
    object Loading: GetUrlProfileManagementResult()
    class Success(val url: String): GetUrlProfileManagementResult()
    class Failed(val throwable: Throwable): GetUrlProfileManagementResult()
}
