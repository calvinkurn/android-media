package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.CarouselMapper
import com.tokopedia.sellerhomecommon.domain.model.GetCarouselDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetCarouselDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: CarouselMapper
) : BaseGqlUseCase<List<CarouselDataUiModel>>() {

    override suspend fun executeOnBackground(): List<CarouselDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetCarouselDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetCarouselDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCarouselDataResponse>()
            val carouselData = data.carouselData.data
            return mapper.mapRemoteModelToUiModel(carouselData)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val DATA_KEY = "dataKey"
        private const val PAGE = "page"
        private const val LIMIT = "limit"
        private const val NUMBER_OF_LIMIT = 5

        fun getRequestParams(dataKeys: List<String>): RequestParams {
            val mapList: List<Map<String, Any>> = dataKeys.map {
                mapOf(
                        DATA_KEY to it,
                        PAGE to 1,
                        LIMIT to NUMBER_OF_LIMIT
                )
            }
            return RequestParams.create().apply {
                putObject(DATA_KEY, mapList)
            }
        }

        private const val QUERY = """query getCarouselWidgetData(${'$'}dataKey: [CarouselDataKey!]!) {
                   getCarouselWidgetData(dataKey: ${'$'}dataKey) {\n" +
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
                }"""
    }
}