package com.tokopedia.loginregister.login.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.di.LoginQueryConstant
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifData
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifPojo
import javax.inject.Inject

/**
 * Created by Ade Fulki on 17/09/20.
 */

class RegisterPushNotifUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<RegisterPushNotifPojo>(graphqlRepository) {

    fun executeCoroutines(
            publicKey: String,
            signature: String,
            datetime: String,
            onSuccess: (RegisterPushNotifData) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        rawQueries[LoginQueryConstant.QUERY_REGISTER_PUSH_NOTIF]?.let { query ->
            setRequestParams(mapOf(
                    LoginQueryConstant.PARAM_PUBLIC_KEY to publicKey,
                    LoginQueryConstant.PARAM_SIGNATURE to signature,
                    LoginQueryConstant.PARAM_DATETIME to datetime
            ))
            setTypeClass(RegisterPushNotifPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess.invoke(it.data)
            }, {
                onError.invoke(it)
            })
        }
    }

}