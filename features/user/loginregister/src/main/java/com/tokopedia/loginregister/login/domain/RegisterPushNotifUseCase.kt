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

data class RegisterPushNotifParamsModel(
    var publicKey: String = "",
    var signature: String = "",
    var datetime: String = ""
)

class RegisterPushNotifUseCase @Inject constructor(
    val repository: GraphqlRepository,
    coroutineDispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterPushNotifParamsModel, RegisterPushNotifPojo>(coroutineDispatchers.io) {

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: RegisterPushNotifParamsModel): RegisterPushNotifPojo {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(params: RegisterPushNotifParamsModel): Map<String, Any> = mapOf(
        PARAM_PUBLIC_KEY to params.publicKey,
        PARAM_SIGNATURE to params.signature,
        PARAM_DATETIME to params.datetime
    )

    companion object {
        private const val PARAM_SIGNATURE = "signature"
        private const val PARAM_DATETIME = "datetime"
        private const val PARAM_PUBLIC_KEY = "publicKey"

        var query = """
            query registerPushnotif(${'$'}publicKey : String!,${'$'}signature : String!,${'$'}datetime : String!) {
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
    }
}