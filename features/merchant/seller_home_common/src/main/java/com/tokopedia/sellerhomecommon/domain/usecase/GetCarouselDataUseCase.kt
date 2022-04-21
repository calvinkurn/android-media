package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetCarouselData
import com.tokopedia.sellerhomecommon.domain.mapper.CarouselMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetCarouselDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetCarouselDataUseCase(
    gqlRepository: GraphqlRepository,
    mapper: CarouselMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetCarouselDataResponse, List<CarouselDataUiModel>>(
    gqlRepository, mapper, dispatchers, GqlGetCarouselData.QUERY, false
) {

    companion object {

        private const val DATA_KEYS = "dataKeys"
        private const val DEFAULT_PAGE_NUMBER = 1
        private const val DEFAULT_LIMIT = 5

        fun getRequestParams(
            dataKey: List<String>,
            pageNumber: Int = DEFAULT_PAGE_NUMBER,
            limits: Int = DEFAULT_LIMIT
        ): RequestParams {
            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it,
                    jsonParams = DynamicParameterModel(
                        page = pageNumber,
                        limit = limits
                    ).toJsonString()
                )
            }
            return RequestParams.create().apply {
                putObject(DATA_KEYS, dataKeys)
            }
        }
    }

    override val classType: Class<GetCarouselDataResponse>
        get() = GetCarouselDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<CarouselDataUiModel> {
        val gqlRequest = GraphqlRequest(GqlGetCarouselData, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCarouselDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }
}