package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.penalty.domain.response.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopPenaltySummaryTypesUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : UseCase<ShopPenaltySummaryTypeWrapper>() {

    companion object {
        const val SHOP_SCORE_PENALTY_TYPES_INPUT = "inputTypes"
        const val SHOP_SCORE_PENALTY_SUMMARY_INPUT = "inputSummary"

        const val INPUT_PARAM = "input"

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

        val SHOP_SCORE_PENALTY_SUMMARY_QUERY = """
            query shopScorePenaltySummary(${'$'}input: ShopScorePenaltySummaryParam!) {
               shopScorePenaltySummary(input: ${'$'}input) {
                 result {
                   penalty
                   penaltyAmount
                 }
                 error {
                   message
                 }
               }
             }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopScorePenaltyTypesParam: ShopScorePenaltyTypesParam, shopScorePenaltySummaryParam: ShopScorePenaltySummaryParam): RequestParams = RequestParams.create().apply {
            putObject(SHOP_SCORE_PENALTY_TYPES_INPUT, shopScorePenaltyTypesParam)
            putObject(SHOP_SCORE_PENALTY_SUMMARY_QUERY, shopScorePenaltySummaryParam)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopPenaltySummaryTypeWrapper {
        val shopScorePenaltyTypesInput = requestParams.getObject(SHOP_SCORE_PENALTY_TYPES_INPUT) as? ShopScorePenaltyTypesParam
        val shopScorePenaltyParam = mapOf(INPUT_PARAM to shopScorePenaltyTypesInput)
        val shopScorePenaltyRequest = GraphqlRequest(SHOP_SCORE_PENALTY_TYPES_QUERY, ShopScorePenaltyTypesResponse::class.java, shopScorePenaltyParam)

        val shopPenaltySummaryInput = requestParams.getObject(SHOP_SCORE_PENALTY_SUMMARY_INPUT) as? ShopScorePenaltySummaryParam
        val shopPenaltySummaryParam = mapOf(INPUT_PARAM to shopPenaltySummaryInput)
        val shopPenaltyRequest = GraphqlRequest(SHOP_SCORE_PENALTY_SUMMARY_QUERY, ShopScorePenaltySummaryResponse::class.java, shopPenaltySummaryParam)

        val requests = listOf(shopScorePenaltyRequest, shopPenaltyRequest)

        val shopPenaltySummaryTypeWrapper = ShopPenaltySummaryTypeWrapper()

        try {
            val gqlResponse = gqlRepository.getReseponse(requests)
            if (gqlResponse.getError(ShopScorePenaltySummaryResponse::class.java).isNullOrEmpty()) {
                val penaltySummaryData = gqlResponse.getData<ShopScorePenaltySummaryResponse>(ShopScorePenaltySummaryResponse::class.java).shopScorePenaltySummary
                shopPenaltySummaryTypeWrapper.shopScorePenaltySummaryResponse = penaltySummaryData
            }

            if (gqlResponse.getError(ShopScorePenaltyTypesResponse::class.java).isNullOrEmpty()) {
                val penaltyTypeData = gqlResponse.getData<ShopScorePenaltyTypesResponse>(ShopScorePenaltyTypesResponse::class.java).shopScorePenaltyTypes
                shopPenaltySummaryTypeWrapper.shopScorePenaltyTypesResponse = penaltyTypeData
            } else {
                val penaltyTypeError = gqlResponse.getError(ShopScorePenaltyTypesResponse::class.java).joinToString(prefix = ",") { it.message }
                throw MessageErrorException(penaltyTypeError)
            }
        } catch (e: Throwable) {
            throw MessageErrorException(e.message)
        }

        return shopPenaltySummaryTypeWrapper
    }
}