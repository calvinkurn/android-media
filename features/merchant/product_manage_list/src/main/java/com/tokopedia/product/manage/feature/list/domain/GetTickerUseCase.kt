package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.data.model.GetTargetedTickerResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetTickerProductManageQuery", GetTickerUseCase.QUERY)
class GetTickerUseCase @Inject constructor(repository: GraphqlRepository) :
    GraphqlUseCase<GetTargetedTickerResponse>(repository) {

    companion object {

        private const val PAGE_VALUE = "icarus.manage-product"
        private const val PARAM_PAGE = "page"
        private const val PARAM_TARGET = "target"

        const val QUERY = """
            query TickerUnification(${'$'}page: String!, ${'$'}target: [GetTargetedTickerRequestTarget]!) {
              GetTargetedTicker(input:{Page: ${'$'}page, Target: ${'$'}target}) {
                List {
                  ID
                  Title
                  Content
                  Action {
                    Label
                    Type
                    AppURL
                    WebURL
                  }
                  Type
                  Priority
                }
              }
            }
        """

        private fun createRequestParams(): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_PAGE, PAGE_VALUE)
                putObject(PARAM_TARGET, emptyArray<String>())
            }
        }
    }

    init {
        setGraphqlQuery(GetTickerProductManageQuery())
        setTypeClass(GetTargetedTickerResponse::class.java)
    }

    suspend fun execute(): GetTargetedTickerResponse {
        val requestParams = createRequestParams()
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}
