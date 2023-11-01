package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyDetailMergeResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

open class GetShopPenaltyDetailMergeUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val getOngoingPenaltyDateUseCase: GetOngoingPenaltyDateUseCase
) : UseCase<Pair<ShopPenaltySummaryTypeWrapper, ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail>>() {

    override suspend fun executeOnBackground(): Pair<ShopPenaltySummaryTypeWrapper, ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail> {
        val status = params.getInt(STATUS_KEY, ShopScoreConstant.STATUS_ONGOING)

        val (actualStartDate, actualEndDate) = getOngoingPenaltyDateUseCase.execute(
            startDate,
            endDate,
            status
        )

        params.run {
            putString(START_DATE_KEY, actualStartDate)
            putString(END_DATE_KEY, actualEndDate)
        }

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
            return shopScorePenaltySummaryWrapper to penaltyDetailResponse
        } catch (e: IOException) {
            throw IOException(e.message)
        } catch (e: Exception) {
            val error = gqlResponse.getError(ShopScorePenaltyDetailResponse::class.java)
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(", "))
        }
    }

    private var params = RequestParams.create()
    private var startDate = String.EMPTY
    private var endDate = String.EMPTY

    fun setParams(
        startDate: String,
        endDate: String,
        typeIds: List<Int>,
        sort: Int,
        status: Int
    ) {
        this@GetShopPenaltyDetailMergeUseCase.startDate = startDate
        this@GetShopPenaltyDetailMergeUseCase.endDate = endDate
        params = RequestParams.create().apply {
            putObject(TYPE_IDS_KEY, typeIds)
            putInt(SORT_KEY, sort)
            putInt(STATUS_KEY, status)
        }
    }

    companion object {

        private const val START_DATE_KEY = "startDate"
        private const val END_DATE_KEY = "endDate"
        private const val TYPE_IDS_KEY = "typeIDs"
        private const val SORT_KEY = "sort"
        private const val STATUS_KEY = "status"

        val SHOP_SCORE_PENALTY_DETAIL_MERGE_QUERY = """   
            query shopScorePenaltyDetail(
                ${'$'}startDate: String!,
                ${'$'}endDate: String!,
                ${'$'}typeIDs: [Int64],
                ${'$'}sort: Int!,
                ${'$'}status: Int!
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
                   penaltyDynamic
                   orderVerified
                   shopLevel
                   penaltyCumulativePercentage
                   penaltyCumulativePercentageFormatted
                   conversionData {
                    cumulativePercentage
                    cumulativePercentageFormatted
                    penaltyPoint
                    conversionRateFlag
                   }
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
                   typeIDs : ${'$'}typeIDs
                   sort : ${'$'}sort
                   status: ${'$'}status
                   lang : "id"
                   source: "android-shop-penalty"
                 }
               ) {
                  startDate
                  endDate
                  defaultStartDate
                  defaultEndDate
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
                      penaltyTypeGroup
                      productDetail {
                        id
                        name
                      }
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
