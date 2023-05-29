package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

class GetShopPenaltyDetailUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : UseCase<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail>() {

    companion object {
        private const val SHOP_SCORE_PENALTY_DETAIL_INPUT = "input"

        val SHOP_SCORE_PENALTY_DETAIL_QUERY = """
            query shopScorePenaltyDetail(${'$'}input: ShopScorePenaltyDetailParam!) {
               shopScorePenaltyDetail(input: ${'$'}input) {
                  result {
                      shopPenaltyID
                      invoiceNumber
                      reason
                      score
                      createTime
                      typeID
                      typeName
                      penaltyStartDate
                      penaltyExpirationDate
                      status
                  }
                  hasNext
                  hasPrev
                  error {
                    message
                  }
               }
             }
        """.trimIndent()


        fun crateParams(shopScorePenaltyDetailParam: ShopScorePenaltyDetailParam): Map<String, Any> =
            mapOf(SHOP_SCORE_PENALTY_DETAIL_INPUT to shopScorePenaltyDetailParam)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail {
        val shopScorePenaltyDetailRequest = GraphqlRequest(
            SHOP_SCORE_PENALTY_DETAIL_QUERY,
            ShopScorePenaltyDetailResponse::class.java,
            params
        )
        val gqlResponse = gqlRepository.response(listOf(shopScorePenaltyDetailRequest))
        try {
            return gqlResponse.getData<ShopScorePenaltyDetailResponse>(
                ShopScorePenaltyDetailResponse::class.java
            ).shopScorePenaltyDetail
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Exception) {
            val error = gqlResponse.getError(ShopScorePenaltyDetailResponse::class.java)
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(", "))
        }
    }
}

