package com.tokopedia.loginregister.login.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifPojo
import javax.inject.Inject

/**
 * Created by Ade Fulki on 17/09/20.
 */

data class RegisterPushNotificationParamsModel(
    var publicKey: String = "",
    var signature: String = "",
    var datetime: String = ""
)

class RegisterPushNotificationUseCase @Inject constructor(
    val repository: GraphqlRepository,
    coroutineDispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterPushNotificationParamsModel, RegisterPushNotifPojo>(coroutineDispatchers.io) {

    override suspend fun execute(params: RegisterPushNotificationParamsModel): RegisterPushNotifPojo {
        return repository.request(graphqlQuery(), mapParams(params))
    }

    private fun mapParams(params: RegisterPushNotificationParamsModel): Map<String, Any> = mapOf(
        PARAM_PUBLIC_KEY to params.publicKey,
        PARAM_SIGNATURE to params.signature,
        PARAM_DATETIME to params.datetime
    )

    override fun graphqlQuery(): String = """
        query registerPushnotif(${'$'}publicKey : String!, ${'$'}signature : String!, ${'$'}datetime : String!) {
          RegisterPushnotif(
            publicKey: ${'$'}publicKey
            signature: ${'$'}signature
            datetime: ${'$'}datetime
          ){
            success
            errorMessage
            message
          }
        }
    """.trimIndent()

    companion object {
        private const val PARAM_SIGNATURE = "signature"
        private const val PARAM_DATETIME = "datetime"
        private const val PARAM_PUBLIC_KEY = "publicKey"
    }

}