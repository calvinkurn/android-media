package com.tokopedia.loginregister.login.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.di.LoginQueryConstant
import com.tokopedia.loginregister.login.domain.pojo.StatusPinData
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-10.
 * ade.hadian@tokopedia.com
 */

open class StatusPinUseCase @Inject constructor(
        private val rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<StatusPinPojo>(graphqlRepository) {

    fun executeCoroutines(onSuccess: (StatusPinData) -> kotlin.Unit, onError: (kotlin.Throwable) -> kotlin.Unit){
        rawQueries[LoginQueryConstant.QUERY_STATUS_PIN]?.let { query ->
            setTypeClass(StatusPinPojo::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it.data)
            }, onError)
        }
    }
}