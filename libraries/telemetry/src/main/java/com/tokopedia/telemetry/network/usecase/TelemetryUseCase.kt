package com.tokopedia.telemetry.network.usecase

import android.util.Log
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
import javax.inject.Inject

class TelemetryUseCase @Inject constructor(
    val repository: dagger.Lazy<GraphqlRepository>,
    private val rsa: RSA,
    private val aesEncryptorGCM: AESEncryptorGCM
) {

    var publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvagLv5bi5Edp2MQQTNps" +
            "8XBGMqDgyDA8xn+lKTDNvbm6sP8Ij0r4KvRod6atedQd74OpURY2COX7Tt035zRG" +
            "b47/9wY+AJxOM//wXEBqZAVAv0GQMwnqDzoel4uLlixm/wDqjg+tMprd9EYAplTU" +
            "cqa5eg+rO4l8n/pl8Fu7bYUrS6SC4027K9xgRG0pjEWHhdKI8CqtA+KzntQ8INAI" +
            "jn9xrsDC5MOnRk7TeKXhbaOeFQXnV9ZUacnEEnNVoVneGtKcDiLAcUdqPHQIa2dA" +
            "2K0uUuEVNXxgn0/8OnLO7Yjh5z/7uL2rHdoFTAIH+iZn0JLCKAjm1f4wGcR4n3+Q" +
            "CwIDAQAB"

    var useCase: GraphqlUseCase<TelemetryResponse>? = null

    companion object {
        const val PARAM_INPUT = "input"
        val query: String =
            """
            mutation subDvcTl(${'$'}input: SubDvcTlRequest!){
              mutationSubDvcTl(input: ${'$'}input) {
                is_error
                data {
                  error_message
                }
              }
            }
        """.trimIndent()
    }

    private fun getOrCreateUseCase(): GraphqlUseCase<TelemetryResponse> {
        val useCaseTemp = useCase
        return if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<TelemetryResponse>(repository.get())
            newUseCase.setGraphqlQuery(query)
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
        Log.w("HENDRYTAG", "telemetry key $key")
        Log.w("HENDRYTAG", "telemetry secret key $secretKey")
        Log.w("HENDRYTAG", "telemetry json " + telemetrySection.toJson())
        val params: Map<String, Any?> = mutableMapOf(
            PARAM_INPUT to MutationSubDvcTlRequest(
                telemetrySection.eventName + "-" + telemetrySection.eventNameEnd,
                aesEncryptorGCM.encrypt(telemetrySection.toJson(), secretKey),
                (telemetrySection.startTime / 1000L).toInt(),
                (telemetrySection.endTime / 1000L).toInt(),
                rsa.encrypt(
                    key, rsa.stringToPublicKey(publicKeyString),
                    Constants.RSA_OAEP_ALGORITHM
                )
            )
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }

    private fun generateRandom32Byte(): String {
        val source = (('0'..'9') + ('a'..'z') + ('A'..'Z')).toList()
        return (1..32).map { source.random() }.joinToString("")
    }
}