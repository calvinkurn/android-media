package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
        mapper: CarouselMapper
) : CloudAndCacheGraphqlUseCase<GetCarouselDataResponse, List<CarouselDataUiModel>>(gqlRepository, mapper, true, GetCarouselDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
    }

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

        private val QUERY = """
            query getCarouselWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchCarouselWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  dataKey
                  items {
                    ID
                    URL
                    CreativeName
                    AppLink
                    FeaturedMediaURL
                  }
                  errorMsg
                }
              }
            }
        """.trimIndent()
    }
}