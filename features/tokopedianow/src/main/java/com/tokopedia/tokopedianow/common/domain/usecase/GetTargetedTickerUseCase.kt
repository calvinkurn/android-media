package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopedianow.common.domain.query.GetTargetedTickerQuery
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.home.domain.request.GetTargetedTickerRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTargetedTickerUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetTargetedTickerResponse>(graphqlRepository) {

    companion object {
        const val PARAM_INPUT = "input"
        const val HOME_PAGE = "marketplace.now-home"
        const val CATEGORY_PAGE = "marketplace.now-category"
        const val SEARCH_PAGE = "marketplace.now-search"
        const val CATEGORY_L2 = "marketplace.now-category-l2"
        const val TARGET_TYPE = "warehouse_id"

        fun createGetTargetedTickerRequest(page: String, warehouseId: String) = GraphqlRequest(
            GetTargetedTickerQuery,
            GetTargetedTickerResponse::class.java,
            variables = mapOf(
                PARAM_INPUT to GetTargetedTickerRequest(
                    page = page,
                    targets = listOf(
                        GetTargetedTickerRequest.Target(
                            type = TARGET_TYPE,
                            value = arrayListOf(warehouseId)
                        )
                    )
                )
            )
        )

        fun getTargetedTickerResponse(graphqlResponse: GraphqlResponse): GetTargetedTickerResponse = graphqlResponse.getData(GetTargetedTickerResponse::class.java)
    }

    init {
        setGraphqlQuery(GetTargetedTickerQuery)
        setTypeClass(GetTargetedTickerResponse::class.java)
    }

    suspend fun execute(
        page: String,
        warehouseId: String
    ): GetTargetedTickerResponse {
        val targetRequest = GetTargetedTickerRequest(
            page = page,
            targets = listOf(
                GetTargetedTickerRequest.Target(
                    type = TARGET_TYPE,
                    value = arrayListOf(warehouseId)
                )
            )
        )
        setRequestParams(RequestParams.create().apply {
            putObject(PARAM_INPUT, targetRequest)
        }.parameters)
        return executeOnBackground()
    }
}
