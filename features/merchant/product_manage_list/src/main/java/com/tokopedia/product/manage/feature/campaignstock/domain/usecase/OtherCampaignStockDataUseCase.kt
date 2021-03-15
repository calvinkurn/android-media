package com.tokopedia.product.manage.feature.campaignstock.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.feature.campaignstock.domain.model.param.OtherCampaignStockParam
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class OtherCampaignStockDataUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): GraphqlUseCase<OtherCampaignStockData>(gqlRepository) {

    companion object {
        private const val QUERY = "query GetOtherCampaignStockData(\$productID: String!, \$options: OptionV3!, \$extraInfo:ExtraInfoV3!, \$warehouseID: String) {\n" +
                "  getProductV3(productID: \$productID, options: \$options, extraInfo:${'$'}extraInfo, warehouseID: ${'$'}warehouseID) {\n" +
                "    pictures {\n" +
                "      urlThumbnail\n" +
                "    }\n" +
                "    status\n" +
                "    campaign{\n" +
                "      isActive\n" +
                "    }"+
                "  }\n" +
                "}"

        private const val PRODUCT_ID_KEY = "productID"
        private const val OPTIONS_KEY = "options"
        private const val EXTRA_INFO_KEY = "extraInfo"
        private const val EVENT_KEY = "event"
        private const val WAREHOUSE_ID_KEY = "warehouseID"

        @JvmStatic
        fun createRequestParams(productId: String, warehouseId: String? = null): RequestParams =
                RequestParams.create().apply {
                    val extraInfoParam = RequestParams().apply {
                        putBoolean(EVENT_KEY, true)
                    }.parameters

                    putString(PRODUCT_ID_KEY, productId)
                    putObject(OPTIONS_KEY, OtherCampaignStockParam())
                    putObject(EXTRA_INFO_KEY, extraInfoParam)
                    putObject(WAREHOUSE_ID_KEY, warehouseId)
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): OtherCampaignStockData {
        val gqlRequest = GraphqlRequest(QUERY, OtherCampaignStockResponse::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))
        val errors = gqlResponse.getError(OtherCampaignStockResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<OtherCampaignStockResponse>(OtherCampaignStockResponse::class.java)
            return data.otherCampaignStockData
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }
}