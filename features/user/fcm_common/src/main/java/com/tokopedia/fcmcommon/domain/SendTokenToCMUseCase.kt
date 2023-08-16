package com.tokopedia.fcmcommon.domain

import com.tokopedia.fcmcommon.common.FcmConstant
import com.tokopedia.fcmcommon.data.CmAddToken
import com.tokopedia.fcmcommon.data.TokenResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.Dispatchers
import java.util.*
import javax.inject.Inject

class SendTokenToCMUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<HashMap<String, Any>, TokenResponse>(Dispatchers.IO) {

    override fun graphqlQuery(): String {
        return """
            mutation AddToken(${'$'}userId: Int, ${'$'}appId: String, 
            ${'$'}notificationToken: String, ${'$'}deviceOS: String, ${'$'}sdkVersion: String, 
            ${'$'}macAddress: String, ${'$'}appVersion: String, ${'$'}requestTimestamp: String, 
            ${'$'}loggedStatus: String, ${'$'}appName: String) {
            cmAddToken(input: {userId: ${'$'}userId, appId: ${'$'}appId, 
              notificationToken: ${'$'}notificationToken, deviceOS: ${'$'}deviceOS, 
              sdkVersion: ${'$'}sdkVersion, macAddress: ${'$'}macAddress, 
              appVersion: ${'$'}appVersion, requestTimestamp: ${'$'}requestTimestamp, 
              loggedStatus: ${'$'}loggedStatus, appName: ${'$'}appName}) {
                UserId
                Error
              }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: HashMap<String, Any>): TokenResponse {
        try {
            return repository.request(graphqlQuery(), params)
        } catch (e: MessageErrorException) {
            val errorStr = e.message
            if (errorStr.isNullOrEmpty()) {
                ServerLogger.log(
                    Priority.P2,
                    "CM_VALIDATION",
                    mapOf(
                        "type" to "validation",
                        "reason" to "cm_gql_error_thrown",
                        "data" to ""
                    )
                )
            } else {
                ServerLogger.log(
                    Priority.P2,
                    "CM_VALIDATION",
                    mapOf(
                        "type" to "validation",
                        "reason" to "cm_gql_error_thrown",
                        "data" to errorStr.take(FcmConstant.MAX_LIMIT)
                    )
                )
            }
            return TokenResponse(cmAddToken = CmAddToken(error = errorStr, userId = 0))
        }
    }
}
