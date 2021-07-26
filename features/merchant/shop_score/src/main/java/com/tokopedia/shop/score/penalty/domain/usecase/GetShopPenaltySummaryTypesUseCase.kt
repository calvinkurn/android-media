package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.penalty.domain.response.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class GetShopPenaltySummaryTypesUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository
) : UseCase<ShopPenaltySummaryTypeWrapper>() {

    companion object {
        const val INPUT_START_DATE = "inputStartDate"
        const val INPUT_END_DATE = "inputEndDate"

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
        fun createParams(startDate: String, endDate: String): RequestParams = RequestParams.create().apply {
            putString(INPUT_START_DATE, startDate)
            putString(INPUT_END_DATE, endDate)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopPenaltySummaryTypeWrapper {
        val startDate = requestParams.getString(INPUT_START_DATE, "")
        val endDate = requestParams.getString(INPUT_END_DATE, "")
        val shopScorePenaltyParam = mapOf(INPUT_PARAM to ShopScorePenaltyTypesParam())
        val shopScorePenaltyRequest = GraphqlRequest(SHOP_SCORE_PENALTY_TYPES_QUERY, ShopScorePenaltyTypesResponse::class.java, shopScorePenaltyParam)

        val shopPenaltySummaryParam = mapOf(INPUT_PARAM to ShopScorePenaltySummaryParam(startDate = startDate, endDate = endDate))
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
            if (e is SocketTimeoutException || e is UnknownHostException) {
                throw IOException(e.message)
            } else {
                throw Exception(e.message)
            }
        }

        return shopPenaltySummaryTypeWrapper
    }
}