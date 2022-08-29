package com.tokopedia.telemetry.network.usecase

import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Constants
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.telemetry.model.TelemetrySection
import com.tokopedia.telemetry.network.data.MutationSubDvcTlRequest
import com.tokopedia.telemetry.network.data.TelemetryResponse
import com.tokopedia.telemetry.network.query.TelemetryQuery
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import java.security.interfaces.RSAPublicKey
import javax.inject.Inject

class TelemetryUseCase @Inject constructor(
    val repository: dagger.Lazy<GraphqlRepository>,
    private val rsa: RSA,
    private val aesEncryptorGCM: AESEncryptorGCM
) {

    var useCase: GraphqlUseCase<TelemetryResponse>? = null

    companion object {
        var rsaPublicKey: RSAPublicKey? = null
        const val PARAM_INPUT = "input"

        val publicKeyString
            get() = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvagLv5bi5Edp2MQQTNps" +
                        "8XBGMqDgyDA8xn+lKTDNvbm6sP8Ij0r4KvRod6atedQd74OpURY2COX7Tt035zRG" +
                        "b47/9wY+AJxOM//wXEBqZAVAv0GQMwnqDzoel4uLlixm/wDqjg+tMprd9EYAplTU" +
                        "cqa5eg+rO4l8n/pl8Fu7bYUrS6SC4027K9xgRG0pjEWHhdKI8CqtA+KzntQ8INAI" +
                        "jn9xrsDC5MOnRk7TeKXhbaOeFQXnV9ZUacnEEnNVoVneGtKcDiLAcUdqPHQIa2dA" +
                        "2K0uUuEVNXxgn0/8OnLO7Yjh5z/7uL2rHdoFTAIH+iZn0JLCKAjm1f4wGcR4n3+Q" +
                        "CwIDAQAB"
            } else {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxi18HP3m9M6Su0D7mk4n" +
                        "n7dbIid3P6vPnQv1wUfuDvhl9YcPxmizbyFEY/d/YYFls/+hhUrQkrASSUThD965" +
                        "orv5CptFAiSYFLx2KAdO0VsrB3IBc8MDQiBak99GLqTDWGsU8ARyc8Mo/thG9hDx" +
                        "c3f6Z7dI1ene4T+dINgUllWUI/fVouqg//Q6wk3w2r0VKYeLn72BvXr2YFtj6WNe" +
                        "FDoF+mtrKfseqXIO4MtelBxKSBl/9dTbo9zon23bhrk2nRqxUtig3Uq2sYs7UNkZ" +
                        "bjaNuTV0SQpzDVYiDjk/mfH6v1b8md9XlhTvguyeTxD+2tv3OJKV0468mAg7y44J" +
                        "gwIDAQAB"
            }
    }

    private fun getOrCreateUseCase(): GraphqlUseCase<TelemetryResponse> {
        val useCaseTemp = useCase
        return if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<TelemetryResponse>(repository.get())
            newUseCase.setGraphqlQuery(TelemetryQuery())
            newUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            )
            newUseCase.setTypeClass(TelemetryResponse::class.java)
            useCase = newUseCase
            newUseCase
        } else {
            useCaseTemp
        }
    }

    suspend fun execute(telemetrySection: TelemetrySection): TelemetryResponse {
        val useCase = getOrCreateUseCase()
        val key = generateRandom32Byte()
        val secretKey = aesEncryptorGCM.generateKey(key)

        val params: Map<String, Any?> = mutableMapOf(
            PARAM_INPUT to MutationSubDvcTlRequest(
                telemetrySection.eventName + "-" + telemetrySection.eventNameEnd,
                aesEncryptorGCM.encrypt(telemetrySection.toJson(), secretKey).replace("\n", ""),
                (telemetrySection.startTime / 1000L).toInt(),
                (telemetrySection.endTime / 1000L).toInt(),
                rsa.encrypt(
                    key, getPublicKey(),
                    Constants.RSA_OAEP_ALGORITHM
                ).replace("\n", "")
            )
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }

    private fun getPublicKey(): RSAPublicKey {
        val key = rsaPublicKey
        if (key == null) {
            val k = rsa.stringToPublicKey(publicKeyString)
            rsaPublicKey = k
            return k
        } else {
            return key
        }
    }

    private fun generateRandom32Byte(): String {
        val source = (('0'..'9') + ('a'..'z') + ('A'..'Z')).toList()
        return (1..32).map { source.random() }.joinToString("")
    }
}