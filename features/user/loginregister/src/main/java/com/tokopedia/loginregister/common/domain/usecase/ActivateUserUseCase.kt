package com.tokopedia.loginregister.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.query.QueryActivateUser
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Ade Fulki on 16/12/20.
 */

class ActivateUserUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<ActivateUserPojo>
) : UseCase<ActivateUserPojo>() {

    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(QueryActivateUser.query)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(ActivateUserPojo::class.java)
    }

    override suspend fun executeOnBackground(): ActivateUserPojo {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParams(
            email: String = "",
            validateToken: String = ""
    ) {
        params.putString(PARAM_EMAIL, email)
        params.putString(PARAM_VALIDATE_TOKEN, validateToken)
    }

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_VALIDATE_TOKEN = "validateToken"
    }
}