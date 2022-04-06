package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.home.domain.mapper.LocationParamMapper
import com.tokopedia.tokopedianow.home.domain.model.TickerResponse
import com.tokopedia.tokopedianow.home.domain.query.GetTicker.QUERY
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTickerUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
): GraphqlUseCase<TickerResponse>(graphqlRepository) {

    companion object {
        const val PARAM_PAGE = "page"
        const val PARAM_LOCATION = "location"
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(TickerResponse::class.java)
    }

    suspend fun execute(pageSource: String = "tokonow", localCacheModel: LocalCacheModel?): TickerResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_PAGE, pageSource)
            putString(PARAM_LOCATION, LocationParamMapper.mapLocation(localCacheModel))
        }.parameters)
        return executeOnBackground()
    }
}