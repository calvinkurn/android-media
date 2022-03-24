package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetReferralSenderResponse
import com.tokopedia.tokopedianow.home.domain.query.GetReferralSenderHome
import com.tokopedia.usecase.RequestParams

class GetReferralSenderHomeUseCase(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetReferralSenderResponse>(graphqlRepository) {

    companion object {
        const val SLUG = "slug"
    }

    init {
        setTypeClass(GetReferralSenderResponse::class.java)
        setGraphqlQuery(object : GqlQueryInterface {
                override fun getOperationNameList() = listOf(GetReferralSenderHome.OPERATION_NAME)
                override fun getQuery(): String = GetReferralSenderHome.QUERY
                override fun getTopOperationName() = GetReferralSenderHome.OPERATION_NAME
            }
        )
    }

    suspend fun execute(slug: String): GetReferralSenderResponse {
        setRequestParams(RequestParams.create().apply {
            putString(SLUG, slug)
        }.parameters)
        return executeOnBackground()
    }

}