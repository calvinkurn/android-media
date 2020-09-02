package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.TopAdsDepositDataModel
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
                "      title\n" +
                "      detail\n" +
                "      object {\n" +
                "       type\n" +
                "       text\n" +
                "      }\n" +
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
            } else {
                val topAdsDepositErrorObjectMessage = responseError.joinToString {
                    it.errorObject?.errorTextList.let { errorList ->
                        if (errorList.isNullOrEmpty()) {
                            it.detail.orEmpty()
                        } else {
                            errorList.joinToString()
                        }
                    }
                }
                throw MessageErrorException(topAdsDepositErrorObjectMessage)
            }
        }
        throw MessageErrorException(gqlError.joinToString { it.message })
    }
}