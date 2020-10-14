package com.tokopedia.seller.action.balance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.balance.domain.model.SellerActionBalanceResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SliceSellerBalanceUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): UseCase<String>() {

    companion object {
        private const val QUERY = "query SellerActionBalance {\n" +
                "  balance {\n" +
                "    seller_usable\n" +
                "    buyer_usable\n" +
                "  }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): String {
        val request = GraphqlRequest(QUERY, SellerActionBalanceResponse::class.java, RequestParams.EMPTY.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(SellerActionBalanceResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<SellerActionBalanceResponse>(SellerActionBalanceResponse::class.java)
            return data.balance.totalBalance.orEmpty()
        } else {
            throw MessageErrorException(error.joinToString())
        }
    }
}