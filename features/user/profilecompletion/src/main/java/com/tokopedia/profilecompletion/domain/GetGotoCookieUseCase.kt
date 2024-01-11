package com.tokopedia.profilecompletion.domain

import android.util.Base64
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.data.SeamlessData
import com.tokopedia.profilecompletion.data.SecretData
import com.tokopedia.profilecompletion.di.ProfileManagementApi
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.time.TimeHelper
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class GetGotoCookieUseCase @Inject constructor(
    private val profileManagementApi: ProfileManagementApi,
    private val userSessionInterface: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, GetGotoCookieResult>(dispatchers.io) {
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: String): GetGotoCookieResult {
        val secretResponse = profileManagementApi.getSecret(MODULE_NAME_SEAMLESS).body()

        val result = if (secretResponse?.success == true) {
            return getSeamless(url = params, secretData = secretResponse.data)
        } else {
            val throwable = MessageErrorException(secretResponse.toString())
            GetGotoCookieResult.Failed(throwable)
        }

        return result
    }

    private suspend fun getSeamless(url: String, secretData: SecretData): GetGotoCookieResult {
        val timeRfc3339 = getRimeRfc3339()
        val authorization = getHmacSignature(
            timeRfc3339 = timeRfc3339,
            token = secretData.token,
            secretKeyId = secretData.secretKeyId,
            secretKey = secretData.secretKey
        )

        val parameter = mapOf(
            KEY_X_USER_ID to userSessionInterface.userId,
            KEY_X_URL to url,
            KEY_X_BACK_URL to VALUE_BACK_URL,
            KEY_X_LANGUAGE to VALUE_LANGUAGE_IND,
            KEY_X_CLIENT_ID to VALUE_CLIENT_ID,
            KEY_X_TOKEN to secretData.token,
            KEY_X_DATE to timeRfc3339,
            KEY_X_AUTHORIZATION to authorization
        )
        val seamlessResponse = profileManagementApi.postSeamless(parameter).body()

        return if (seamlessResponse?.success == true) {
            GetGotoCookieResult.Success(seamlessResponse.data)
        } else {
            val throwable = MessageErrorException(seamlessResponse.toString())
            GetGotoCookieResult.Failed(throwable)
        }
    }

    private fun getRimeRfc3339(): String {
        val currentTimeStamp = TimeHelper.getNowTimeStamp()
        return TimeHelper.format(currentTimeStamp, FORMAT_RFC_3339)
    }

    private fun getHmacSignature(timeRfc3339: String, token: String, secretKeyId: String, secretKey: String): String {
        val content = createContent(
            token = token,
            userId = userSessionInterface.userId,
            date = timeRfc3339
        )
        val hmacSignature = hmacDigest(content, secretKey)
        return "$secretKeyId:${replaceHmacSignature(hmacSignature)}"
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
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
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
        private const val VALUE_CLIENT_ID = "tokopedia:consumer:android"
        private const val VALUE_URL = "/profile/web/"
    }

}


sealed class GetGotoCookieResult {
    object Loading: GetGotoCookieResult()
    class Success(val seamlessData: SeamlessData): GetGotoCookieResult()
    class Failed(val throwable: Throwable): GetGotoCookieResult()
}
