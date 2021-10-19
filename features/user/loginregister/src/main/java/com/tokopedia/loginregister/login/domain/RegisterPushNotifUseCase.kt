package com.tokopedia.loginregister.login.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.login.di.LoginModule.Companion.NAMED_DISPATCHERS_IO
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifPojo
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

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
    @Named(NAMED_DISPATCHERS_IO)
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<RegisterPushNotifParamsModel, RegisterPushNotifPojo>(coroutineDispatcher) {

    override suspend fun execute(params: RegisterPushNotifParamsModel): RegisterPushNotifPojo {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(params: RegisterPushNotifParamsModel): Map<String, Any> = mapOf(
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