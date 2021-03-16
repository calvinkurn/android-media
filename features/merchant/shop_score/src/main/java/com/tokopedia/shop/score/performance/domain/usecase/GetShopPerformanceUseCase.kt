package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMStatusResponse
import com.tokopedia.shop.score.performance.domain.model.GoldPMGradeBenefitInfoResponse
import com.tokopedia.shop.score.performance.domain.model.ShoScoreLevelParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopPerformanceUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<GoldGetPMStatusResponse>() {

    companion object {
        const val SHOP_ID_STATUS = "shopID"
        const val SHOP_ID_BENEFIT_INFO = "shop_id"
        const val SHOP_SCORE_LEVEL_INPUT = "input"

        val GOLD_PM_STATUS_QUERY = """
            query goldGetPMOSStatus(${'$'}shopID: Int!) {
              goldGetPMOSStatus(shopID:${'$'}shopID, includeOS: false) {
                data {
                  power_merchant {
                    status
                  }
                }
              }
            }
        """.trimIndent()

        val GOLD_PM_GRADE_BENEFIT_INFO_QUERY = """
            query goldGetPMGradeBenefitInfo(${'$'}shop_id: Int!){
                goldGetPMGradeBenefitInfo(shop_id: ${'$'}shop_id, source: "goldmerchant", lang: "id", device: "", fields: []){
                        current_pm_grade {
                          shop_level_actual
                          shop_score_actual
                          grade_name
                          image_badge_url
                          image_badge_background_mobile_url
                        }
                        potential_pm_grade {
                          shop_level_current
                          shop_score_current
                          grade_name
                          image_badge_url               
                          image_badge_background_mobile_url
                        }
                }
            }
        """.trimIndent()

        val GOLD_PM_SHOP_INFO_QUERY = """
            query goldGetPMShopInfo(${'$'}shopID: Int!) {
              goldGetPMShopInfo(shopID:${'$'}shopID, source: "goldmerchant", lang: "id", device: "android") {
                is_new_seller
                is_eligible_pm
              }
            }
        """.trimIndent()

        //need adjust type input
        val SHOP_SCORE_LEVEL_QUERY = """
            query shopScoreLevel(${'$'}input: ShopScoreLevelRequest!){
              shopScoreLevel(input: ${'$'}input){
                result {
                  shopID
                  shopScore
                  shopLevel
                  shopScoreDetail{
                    title
                    identifier
                    value
                    rawValue
                    nextMinValue
                    colorText
                  }
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int, shopScoreLevelParam: ShoScoreLevelParam): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID_STATUS, shopID)
            putObject(SHOP_SCORE_LEVEL_INPUT, shopScoreLevelParam)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GoldGetPMStatusResponse {

        val shopID = requestParams.getInt(SHOP_ID_STATUS, 0)

        val goldPMStatusParam = mapOf(SHOP_ID_STATUS to shopID)
        val goldPMGradeBenefitInfoParam = mapOf(SHOP_ID_BENEFIT_INFO to shopID)
        val goldPMStatusRequest = GraphqlRequest(GOLD_PM_STATUS_QUERY,
                GoldGetPMStatusResponse::class.java, goldPMStatusParam)
        val goldPMGradeBenefitInfoRequest = GraphqlRequest(GOLD_PM_GRADE_BENEFIT_INFO_QUERY,
                GoldPMGradeBenefitInfoResponse::class.java, goldPMGradeBenefitInfoParam)

        val requests = mutableListOf(goldPMStatusRequest, goldPMGradeBenefitInfoRequest)
        try {
            val gqlResponse = gqlRepository.getReseponse(requests)
            if (!gqlResponse.getError(GoldGetPMStatusResponse::class.java).isNullOrEmpty()
                    && !gqlResponse.getError(GoldPMGradeBenefitInfoResponse::class.java).isNullOrEmpty()) {
                val goldPMStatusData = gqlResponse.getData<GoldGetPMStatusResponse>(GoldGetPMStatusResponse::class.java).goldGetPMOSStatus
                val goldPMGradeBenefitInfoData = gqlResponse.getData<GoldPMGradeBenefitInfoResponse>(GoldPMGradeBenefitInfoResponse::class.java).goldGetPMGradeBenefitInfo
            }
        } catch (e: Throwable) { }

        //need adjust because waiting from shop score query
        return GoldGetPMStatusResponse()
    }


}