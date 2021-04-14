package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypesParam
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyTypesResponse
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopScorePenaltyTypesUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : UseCase<ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes>() {

    companion object {
        const val SHOP_SCORE_PENALTY_TYPES_INPUT = "input"

        val SHOP_SCORE_PENALTY_TYPES_QUERY = """   
            query shopScorePenaltyTypes(${'$'}input: ShopScorePenaltyTypesParam!){
              shopScorePenaltyTypes(input: ${'$'}input){
                result{
                  id
                  name
                  description
                  penalty
                }
                error{
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopScorePenaltyTypesParam: ShopScorePenaltyTypesParam): RequestParams = RequestParams.create().apply {
            putObject(SHOP_SCORE_PENALTY_TYPES_INPUT, shopScorePenaltyTypesParam)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopScorePenaltyTypesResponse.ShopScorePenaltyTypes {
        val shopScorePenaltyTypesInput = requestParams.getObject(SHOP_SCORE_PENALTY_TYPES_INPUT) as? ShopScorePenaltyTypesParam
        val shopScorePenaltyParam = mapOf(SHOP_SCORE_PENALTY_TYPES_INPUT to shopScorePenaltyTypesInput)
        val shopScorePenaltyRequest = GraphqlRequest(SHOP_SCORE_PENALTY_TYPES_QUERY, ShopScorePenaltyTypesResponse::class.java, shopScorePenaltyParam)
        val requests = listOf(shopScorePenaltyRequest)
        val gqlResponse = gqlRepository.getReseponse(requests)
        val error = gqlResponse.getError(GraphqlError::class.java)

        try {
            return gqlResponse.getData<ShopScorePenaltyTypesResponse>(ShopScorePenaltyTypesResponse::class.java).shopScorePenaltyTypes
        } catch (e: Throwable) {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }

}