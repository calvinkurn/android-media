package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.TopAdsDepositDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery("TopAdsDashboardDepositGqlQuery", TopAdsDashboardDepositUseCase.QUERY)
class TopAdsDashboardDepositUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : UseCase<Float>() {

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Float {
        val gqlRequest = GraphqlRequest(
            TopAdsDashboardDepositGqlQuery(), TopAdsDepositDataModel::class.java, params
        )
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(TopAdsDepositDataModel::class.java)
        if (gqlError.isNullOrEmpty()) {
            val topAdsDepositResponse: TopAdsDepositDataModel =
                gqlResponse.getData(TopAdsDepositDataModel::class.java)
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

    companion object {

        const val QUERY = """
            query GetTopAdsDeposit(${'$'}shopId: String!) {
              topadsDashboardDepositsV2(shop_id: ${'$'}shopId) {
                data {
                  amount
                }
                errors {
                  code
                  title
                  detail
                  object {
                    type
                    text
                  }
                }
              }
            }
        """

        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: String) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }
}