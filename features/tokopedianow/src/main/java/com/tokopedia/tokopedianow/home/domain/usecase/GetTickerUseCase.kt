package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.TickerResponse
import com.tokopedia.tokopedianow.home.domain.query.GetTicker.QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTickerUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<TickerResponse>(graphqlRepository) {

    companion object {
        const val PAGE = "page"
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(TickerResponse::class.java)
    }

    suspend fun execute(pageSource: String = "tokonow"): TickerResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PAGE, pageSource)
        }.parameters)
        return executeOnBackground()
    }
}