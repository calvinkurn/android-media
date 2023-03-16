package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.home.domain.query.GetTargetedTickerNow
import com.tokopedia.tokopedianow.home.domain.request.GetTargetedTickerRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTargetedTickerUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetTargetedTickerResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PAGE = "page"
        const val PARAM_INPUT = "input"
//        const val PARAM_TARGET = "marketplace.now-search"
//        const val PARAM_TARGET = "target"
//        const val PARAM_TARGET = "target"
    }

    init {
        setGraphqlQuery(GetTargetedTickerNow)
        setTypeClass(GetTargetedTickerResponse::class.java)
    }

    suspend fun execute(pageSource: String = "marketplace.now-home", warehouseId: String = "344061"): GetTargetedTickerResponse {
        val targetRequest = GetTargetedTickerRequest(
            page = pageSource,
            targets = listOf(
                GetTargetedTickerRequest.Target("warehouse_id", arrayListOf(warehouseId))
            )
        )
        setRequestParams(RequestParams.create().apply {
            putObject(PARAM_INPUT, targetRequest)
        }.parameters)
        return executeOnBackground()
    }
}
