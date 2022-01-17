package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyDetailMergeResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

open class GetShopPenaltyDetailMergeUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val penaltyMapper: PenaltyMapper
) : UseCase<PenaltyDataWrapper>() {

    override suspend fun executeOnBackground(): PenaltyDataWrapper {
        val startDate = params.getString(START_DATE_KEY, "")
        val endDate = params.getString(END_DATE_KEY, "")
        val typeId = params.getInt(TYPE_ID_KEY, 0)
        val sortBy = params.getInt(SORT_KEY, 0)

        val shopScorePenaltyDetailRequest = GraphqlRequest(
            SHOP_SCORE_PENALTY_DETAIL_MERGE_QUERY,
            ShopPenaltyDetailMergeResponse::class.java,
            params.parameters
        )
        val gqlResponse = graphqlRepository.response(listOf(shopScorePenaltyDetailRequest))
        try {
            val penaltyDetailMergeResponse = gqlResponse.getData<ShopPenaltyDetailMergeResponse>(
                ShopPenaltyDetailMergeResponse::class.java
            )
            val penaltySummaryResponse = penaltyDetailMergeResponse.shopScorePenaltySummary
            val penaltyTypesResponse = penaltyDetailMergeResponse.shopScorePenaltyTypes
            val shopScorePenaltySummaryWrapper = ShopPenaltySummaryTypeWrapper(
                shopScorePenaltySummaryResponse = penaltySummaryResponse,
                shopScorePenaltyTypesResponse = penaltyTypesResponse
            )
            val penaltyDetailResponse = penaltyDetailMergeResponse.shopScorePenaltyDetail
            return penaltyMapper.mapToPenaltyData(
                shopScorePenaltySummaryWrapper,
                penaltyDetailResponse,
                sortBy,
                typeId,
                Pair(startDate, endDate)
            )
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Exception) {
            val error = gqlResponse.getError(ShopScorePenaltyDetailResponse::class.java)
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(", "))
        }
    }

    private var params = RequestParams.create()

    fun setParams(
        startDate: String,
        endDate: String,
        typeId: Int,
        sort: Int
    ) {
        params = RequestParams.create().apply {
            putString(START_DATE_KEY, startDate)
            putString(END_DATE_KEY, endDate)
            putInt(TYPE_ID_KEY, typeId)
            putInt(SORT_KEY, sort)
        }
    }

    companion object {

        private const val START_DATE_KEY = "startDate"
        private const val END_DATE_KEY = "endDate"
        private const val TYPE_ID_KEY = "typeID"
        private const val SORT_KEY = "sort"

        val SHOP_SCORE_PENALTY_DETAIL_MERGE_QUERY = """   
            query shopScorePenaltyDetail(
                ${'$'}startDate: String!,
                ${'$'}endDate: String!,
                ${'$'}typeID: Int!,
                ${'$'}sort: Int!
              ){
              shopScorePenaltyTypes(
                 input: {
                  lang: "id"
                  source: "android"
                }){
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
              shopScorePenaltySummary(
                 input: {
                 startDate : ${'$'}startDate
                 endDate : ${'$'}endDate
                 source: "android"
                }) {
                 result {
                   penalty
                   penaltyAmount
                 }
                 error {
                   message
                 }
               }
               shopScorePenaltyDetail(
                 input: {
                   page : 1
                   total : 10
                   startDate : ${'$'}startDate
                   endDate : ${'$'}endDate
                   typeID : ${'$'}typeID
                   sort : ${'$'}sort
                   lang : "id"
                   source: "android-shop-penalty"
                 }
               ) {
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
    }

}