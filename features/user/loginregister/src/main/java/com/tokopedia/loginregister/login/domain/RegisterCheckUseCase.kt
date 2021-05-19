package com.tokopedia.loginregister.login.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.di.LoginQueryConstant.PARAM_ID
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.domain.query.LoginQueries
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-31.
 * ade.hadian@tokopedia.com
 */

class RegisterCheckUseCase @Inject constructor(
        rawQueries: Map<String, String>,
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<RegisterCheckPojo>(graphqlRepository){

    init {
        setTypeClass(RegisterCheckPojo::class.java)
        setGraphqlQuery(LoginQueries.registerCheckQuery)
    }

    fun getRequestParams(id: String): Map<String, Any?> = mapOf(
            PARAM_ID to id
    )
}