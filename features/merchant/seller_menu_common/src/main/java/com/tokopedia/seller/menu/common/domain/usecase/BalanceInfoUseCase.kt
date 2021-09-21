package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.OtherBalanceResponse
import com.tokopedia.seller.menu.common.domain.entity.OthersBalance
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class BalanceInfoUseCase @Inject constructor(private val graphQlRepository: GraphqlRepository) : UseCase<OthersBalance>() {

    companion object {
        const val QUERY = "query BalanceInfo {\n" +
                "  balance {\n" +
                "    seller_usable\n" +
                "    buyer_usable\n" +
                "  }\n" +
                "  status\n" + //Don't remove `status` field since it's necessary for refresh token flow
                "}"
    }

    override suspend fun executeOnBackground(): OthersBalance {
        val gqlRequest = GraphqlRequest(QUERY, OtherBalanceResponse::class.java, HashMap<String, Any>())
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(OtherBalanceResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<OtherBalanceResponse>(OtherBalanceResponse::class.java)
            return data.othersBalance
        } else throw MessageErrorException(errors.firstOrNull()?.message)
    }
}