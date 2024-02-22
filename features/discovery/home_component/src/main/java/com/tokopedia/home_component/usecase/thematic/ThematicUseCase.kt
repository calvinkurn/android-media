package com.tokopedia.home_component.usecase.thematic

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class ThematicUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<ThematicResponse>
) : UseCase<ThematicModel>() {
    override suspend fun executeOnBackground(): ThematicModel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(getGraphqlQuery())
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
        graphqlUseCase.setTypeClass(ThematicResponse::class.java)
        graphqlUseCase.setRequestParams(createGraphqlRequestParams(useCaseRequestParams))
        val response = graphqlUseCase.executeOnBackground()
        return ThematicModel.fromResponse(response.getThematic.thematic)
    }

    @GqlQuery(ThematicUsecaseUtil.THEMATIC_QUERY_NAME, ThematicUsecaseUtil.THEMATIC_QUERY)
    private fun getGraphqlQuery(): GqlQueryInterface = ThematicQuery()

    protected open fun createGraphqlRequestParams(requestParams: RequestParams): Map<String, String> {
        return mapOf(
            ThematicUsecaseUtil.THEMATIC_PARAM to requestParams.getString(
                ThematicUsecaseUtil.THEMATIC_PARAM,
                ThematicUsecaseUtil.THEMATIC_PAGE_HOME
            )
        )
    }
}
