package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.ValidateReferralUserResponse
import com.tokopedia.tokopedianow.home.domain.query.ValidateReferralUser
import com.tokopedia.tokopedianow.home.domain.query.ValidateReferralUser.OPERATION_NAME
import com.tokopedia.usecase.RequestParams

class ValidateReferralUserUseCase(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ValidateReferralUserResponse>(graphqlRepository) {

    companion object {
        const val SLUG = "slug"
    }

    init {
        setTypeClass(ValidateReferralUserResponse::class.java)
        setGraphqlQuery(object : GqlQueryInterface {
                override fun getOperationNameList() = listOf(OPERATION_NAME)
                override fun getQuery(): String = ValidateReferralUser.QUERY
                override fun getTopOperationName() = OPERATION_NAME
            }
        )
    }

    suspend fun execute(slug: String): ValidateReferralUserResponse {
        setRequestParams(RequestParams.create().apply {
            putString(SLUG, slug)
        }.parameters)
        return executeOnBackground()
    }

}