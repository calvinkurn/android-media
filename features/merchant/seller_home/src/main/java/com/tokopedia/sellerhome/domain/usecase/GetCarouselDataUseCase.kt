package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhome.domain.mapper.CarouselMapper
import com.tokopedia.sellerhome.domain.model.CarouselDataResponse
import com.tokopedia.sellerhome.view.model.CarouselDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 2020-02-11
 */

class GetCarouselDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val mapper: CarouselMapper
) : BaseGqlUseCase<List<CarouselDataUiModel>>() {

    override suspend fun executeOnBackground(): List<CarouselDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, CarouselDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(CarouselDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<CarouselDataResponse>()
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

        const val QUERY = "query getCarouselWidgetData(\$dataKey: [CarouselDataKey!]!) {\n" +
                "  getCarouselWidgetData(dataKey: \$dataKey) {\n" +
                "    data {\n" +
                "      dataKey\n" +
                "      items {\n" +
                "        ID\n" +
                "        URL\n" +
                "        CreativeName\n" +
                "        AppLink\n" +
                "        FeaturedMediaURL\n" +
                "      }\n" +
                "      errorMsg\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}