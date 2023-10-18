package com.scp.auth.registerpushnotif.domain

import com.scp.auth.registerpushnotif.data.RegisterPushNotifPojo
import com.scp.auth.registerpushnotif.data.RegisterPushNotificationParamsModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created by Ade Fulki on 17/09/20.
 */
class ScpRegisterPushNotifUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    coroutineDispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterPushNotificationParamsModel, RegisterPushNotifPojo>(coroutineDispatchers.io) {

    override suspend fun execute(params: RegisterPushNotificationParamsModel): RegisterPushNotifPojo {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query registerPushnotif($$PARAM_PUBLIC_KEY : String!, $$PARAM_SIGNATURE : String!, $$PARAM_DATETIME : String!) {
          RegisterPushnotif(
            publicKey: $$PARAM_PUBLIC_KEY
            signature: $$PARAM_SIGNATURE
            datetime: $$PARAM_DATETIME
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
