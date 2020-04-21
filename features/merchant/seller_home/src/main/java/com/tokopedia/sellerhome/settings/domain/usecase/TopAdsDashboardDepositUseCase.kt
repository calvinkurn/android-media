package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhome.settings.domain.entity.TopAdsDepositDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TopAdsDashboardDepositUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<Float>(){

    companion object {

        const val QUERY = "query GetTopAdsDeposit(\$shopId: Int!) {\n" +
                "  topadsDashboardDeposits(shop_id: \$shopId) {\n" +
                "    data {\n" +
                "      amount\n" +
                "    }\n" +
                "    errors {\n" +
                "      code\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Float {
        val gqlRequest = GraphqlRequest(QUERY, TopAdsDepositDataModel::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(TopAdsDepositDataModel::class.java)
        if (gqlError.isNullOrEmpty()) {
            val topAdsDepositResponse : TopAdsDepositDataModel = gqlResponse.getData(TopAdsDepositDataModel::class.java)
            val responseError = topAdsDepositResponse.topAdsDashboardDeposits?.errors
            if (responseError.isNullOrEmpty()) {
                topAdsDepositResponse.topAdsDashboardDeposits?.depositData?.amount?.let {
                    return it
                }
            }
        }
        throw ResponseErrorException()
    }
}