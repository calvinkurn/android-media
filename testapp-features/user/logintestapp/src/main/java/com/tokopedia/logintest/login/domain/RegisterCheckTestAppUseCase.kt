package com.tokopedia.logintest.login.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logintest.login.di.LoginTestAppQueryConstant
import com.tokopedia.logintest.login.di.LoginTestAppQueryConstant.PARAM_ID
import com.tokopedia.logintest.login.domain.pojo.RegisterCheckPojo
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-31.
 * ade.hadian@tokopedia.com
 */

class RegisterCheckTestAppUseCase @Inject constructor(
        rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<RegisterCheckPojo>(graphqlRepository){

    init {
        rawQueries[LoginTestAppQueryConstant.MUTATION_REGISTER_CHECK]?.let { query ->
            setTypeClass(RegisterCheckPojo::class.java)
            setGraphqlQuery(query)
        }
    }

    fun getRequestParams(id: String): Map<String, Any?> = mapOf(
            PARAM_ID to id
    )
}