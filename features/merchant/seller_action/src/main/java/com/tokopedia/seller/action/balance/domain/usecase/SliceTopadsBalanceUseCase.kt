package com.tokopedia.seller.action.balance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.balance.domain.model.SellerTopAdsBalanceResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceTopadsBalanceUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): UseCase<String>() {

    companion object {
        private const val QUERY = "query SellerTopAdsBalance(\$shopId: Int!) {\n" +
                "  topadsDashboardDeposits(shop_id: \$shopId) {\n" +
                "    data {\n" +
                "      amount_fmt\n" +
                "    }\n" +
                "  }\n" +
                "}"

        const val SHOP_ID_KEY = "shopId"

        @JvmStatic
        fun createRequestParams(shopId: Int): RequestParams =
                RequestParams.create().apply {
                    putInt(SHOP_ID_KEY, shopId)
                }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): String {
        val request = GraphqlRequest(QUERY, SellerTopAdsBalanceResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(SellerTopAdsBalanceResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<SellerTopAdsBalanceResponse>(SellerTopAdsBalanceResponse::class.java)
            if (data.sellerActionTopadsDashboardDeposits?.errors.isNullOrEmpty()) {
                return data.sellerActionTopadsDashboardDeposits?.data?.amountFmt.orEmpty()
            } else {
                throw MessageErrorException(data.sellerActionTopadsDashboardDeposits?.errors?.joinToString { it.errorDetail.orEmpty() } )
            }
        } else {
            throw MessageErrorException(error.joinToString())
        }
    }
}