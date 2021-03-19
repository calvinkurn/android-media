package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.score.performance.domain.model.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetShopPerformanceUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                                    private val userSession: UserSessionInterface) :
        UseCase<ShopScoreWrapperResponse>() {

    companion object {
        const val SHOP_ID_STATUS_INFO = "shopID"
        const val SHOP_ID_BENEFIT_INFO = "shop_id"
        const val SHOP_SCORE_LEVEL_INPUT = "input"
        const val SHOP_SCORE_TOOLTIP_KEY = "tooltip_key"

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
                        next_monthly_refresh_date
                        current_pm_grade {
                          grade_name
                          image_badge_url
                          image_badge_background_mobile_url
                        }
                        potential_pm_grade {
                          grade_name
                          image_badge_url               
                          image_badge_background_mobile_url
                        }
                }
            }
        """.trimIndent()

        val GOLD_PM_SHOP_INFO_QUERY = """
            query goldGetPMShopInfo(${'$'}shop_id: Int!) {
              goldGetPMShopInfo(shop_id:${'$'}shop_id, source: "goldmerchant", lang: "id", device: "android") {
                is_new_seller
                is_eligible_pm
              }
            }
        """.trimIndent()

        val SHOP_SCORE_LEVEL_QUERY = """
            query shopScoreLevel(${'$'}input: ShopScoreLevelParam!){
              shopScoreLevel(input: ${'$'}input){
                result {
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

        val SHOP_LEVEL_TOOLTIP_QUERY = """
            query shopLevel(${'$'}input: ShopLevelParam!){
              shopLevel(input: ${'$'}input){
                result {
                  period
                  nextUpdate
                  shopLevel
                  itemSold
                  niv
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int, shopScoreLevelParam: ShopScoreLevelParam, shopLevelTooltipParam: ShopLevelTooltipParam): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID_STATUS_INFO, shopID)
            putObject(SHOP_SCORE_LEVEL_INPUT, shopScoreLevelParam)
            putObject(SHOP_SCORE_TOOLTIP_KEY, shopLevelTooltipParam)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopScoreWrapperResponse {

        val shopID = requestParams.getInt(SHOP_ID_STATUS_INFO, 0)
        val shopScoreLevelInput = requestParams.getObject(SHOP_SCORE_LEVEL_INPUT) as? ShopScoreLevelParam
        val shopLevelTooltipInput = requestParams.getObject(SHOP_SCORE_TOOLTIP_KEY) as? ShopLevelTooltipParam

        val shopScoreLevelParam = mapOf(SHOP_SCORE_LEVEL_INPUT to shopScoreLevelInput)
        val shopLevelParam = mapOf(SHOP_SCORE_LEVEL_INPUT to shopLevelTooltipInput)

        val goldPMStatusParam = mapOf(SHOP_ID_STATUS_INFO to shopID)
        val goldPMShopInfoParam = mapOf(SHOP_ID_BENEFIT_INFO to shopID)
        val goldPMGradeBenefitInfoParam = mapOf(SHOP_ID_BENEFIT_INFO to shopID)

        val shopScoreLevelRequest = GraphqlRequest(SHOP_SCORE_LEVEL_QUERY, ShopScoreLevelResponse::class.java, shopScoreLevelParam)
        val shopLevelRequest = GraphqlRequest(SHOP_LEVEL_TOOLTIP_QUERY, ShopLevelTooltipResponse::class.java, shopLevelParam)
        val goldPMGradeBenefitInfoRequest = GraphqlRequest(GOLD_PM_GRADE_BENEFIT_INFO_QUERY,
                GoldPMGradeBenefitInfoResponse::class.java, goldPMGradeBenefitInfoParam)
        val goldPMStatusRequest = GraphqlRequest(GOLD_PM_STATUS_QUERY,
                GoldGetPMStatusResponse::class.java, goldPMStatusParam)
        val goldPMShopInfoRequest = GraphqlRequest(GOLD_PM_SHOP_INFO_QUERY, GoldGetPMShopInfoResponse::class.java, goldPMShopInfoParam)

        val requests = mutableListOf<GraphqlRequest>()

        val shopScoreWrapperResponse = ShopScoreWrapperResponse()

        when {
            userSession.isShopOfficialStore -> {
                requests.add(shopScoreLevelRequest)
                requests.add(shopLevelRequest)
                try {
                    val gqlResponse = gqlRepository.getReseponse(requests)

                    if (!gqlResponse.getError(ShopScoreLevelResponse::class.java).isNullOrEmpty()) {
                        val shopScoreLevelData = gqlResponse.getData<ShopScoreLevelResponse>(ShopScoreLevelResponse::class.java).shopScoreLevel
                        shopScoreWrapperResponse.shopScoreLevelResponse = shopScoreLevelData
                    }
                    if (!gqlResponse.getError(ShopLevelTooltipResponse::class.java).isNullOrEmpty()) {
                        val shopLevelTooltipData = gqlResponse.getData<ShopLevelTooltipResponse>(ShopLevelTooltipResponse::class.java).shopLevel
                        shopScoreWrapperResponse.shopScoreTooltipResponse = shopLevelTooltipData
                    }
                } catch (e: Throwable) {
                }
            }
            else -> {
                with(requests) {
                    add(shopScoreLevelRequest)
                    add(shopLevelRequest)
                    add(goldPMStatusRequest)
                    add(goldPMShopInfoRequest)
                    add(goldPMGradeBenefitInfoRequest)
                }

                try {
                    val gqlResponse = gqlRepository.getReseponse(requests)
                    if (!gqlResponse.getError(ShopScoreLevelResponse::class.java).isNullOrEmpty()) {
                        val shopScoreLevelData = gqlResponse.getData<ShopScoreLevelResponse>(ShopScoreLevelResponse::class.java).shopScoreLevel
                        shopScoreWrapperResponse.shopScoreLevelResponse = shopScoreLevelData
                    }
                    if (!gqlResponse.getError(ShopLevelTooltipResponse::class.java).isNullOrEmpty()) {
                        val shopLevelTooltipData = gqlResponse.getData<ShopLevelTooltipResponse>(ShopLevelTooltipResponse::class.java).shopLevel
                        shopScoreWrapperResponse.shopScoreTooltipResponse = shopLevelTooltipData
                    }
                    if (!gqlResponse.getError(GoldGetPMStatusResponse::class.java).isNullOrEmpty()) {
                        val goldPMStatusData = gqlResponse.getData<GoldGetPMStatusResponse>(GoldGetPMStatusResponse::class.java).goldGetPMOSStatus
                        shopScoreWrapperResponse.goldGetPMStatusResponse = goldPMStatusData
                    }

                    if (!gqlResponse.getError(GoldGetPMShopInfoResponse::class.java).isNullOrEmpty()) {
                        val goldGetPMShopInfoData = gqlResponse.getData<GoldGetPMShopInfoResponse>(GoldGetPMShopInfoResponse::class.java).goldGetPMShopInfo
                        shopScoreWrapperResponse.goldGetPMShopInfoResponse = goldGetPMShopInfoData
                    }

                    if (!gqlResponse.getError(GoldPMGradeBenefitInfoResponse::class.java).isNullOrEmpty()) {
                        val goldPMGradeBenefitInfoData = gqlResponse.getData<GoldPMGradeBenefitInfoResponse>(GoldPMGradeBenefitInfoResponse::class.java).goldGetPMGradeBenefitInfo
                        shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse = goldPMGradeBenefitInfoData
                    }


                } catch (e: Throwable) {

                }
            }
        }
        return shopScoreWrapperResponse
    }
}