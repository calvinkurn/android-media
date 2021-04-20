package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
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
        const val SHOP_ID_REPUTATION = "shop_ids"
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
                          last_updated_date
                        }
                        potential_pm_grade {
                          grade_name
                          image_badge_url               
                        }
                }
            }
        """.trimIndent()

        val GOLD_PM_SHOP_INFO_QUERY = """
            query goldGetPMShopInfo(${'$'}shop_id: Int!) {
              goldGetPMShopInfo(shop_id:${'$'}shop_id, source: "goldmerchant", lang: "id", device: "android") {
                is_new_seller
                shop_age
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

        val RECOMMENDATION_TOOLS_QUERY = """
            query valuePropositionGetRecommendationTools(${'$'}shopID: Int!){
              valuePropositionGetRecommendationTools(shopID: ${'$'}shopID, source:"goldmerchant") {
                 recommendation_tools {
                  title
                  text
                  image_url
                  related_link_applink
                }
              }
            }
        """.trimIndent()

        val REPUTATION_SHOPS_QUERY = """
            query reputation_shops(${'$'}shop_ids: Int!){
                reputation_shops(shop_ids: [${'$'}shop_ids]) {
                    score
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

        val getRecommendationToolsParam = mapOf(SHOP_ID_STATUS_INFO to shopID)
        val reputationShopsParam = mapOf(SHOP_ID_REPUTATION to shopID)

        val shopScoreLevelRequest = GraphqlRequest(SHOP_SCORE_LEVEL_QUERY, ShopScoreLevelResponse::class.java, shopScoreLevelParam)
        val shopLevelRequest = GraphqlRequest(SHOP_LEVEL_TOOLTIP_QUERY, ShopLevelTooltipResponse::class.java, shopLevelParam)
        val goldPMGradeBenefitInfoRequest = GraphqlRequest(GOLD_PM_GRADE_BENEFIT_INFO_QUERY,
                GoldPMGradeBenefitInfoResponse::class.java, goldPMGradeBenefitInfoParam)
        val goldPMStatusRequest = GraphqlRequest(GOLD_PM_STATUS_QUERY,
                GoldGetPMStatusResponse::class.java, goldPMStatusParam)
        val goldPMShopInfoRequest = GraphqlRequest(GOLD_PM_SHOP_INFO_QUERY, GoldGetPMShopInfoResponse::class.java, goldPMShopInfoParam)

        val getRecommendationToolsRequest =
                GraphqlRequest(RECOMMENDATION_TOOLS_QUERY, GetRecommendationToolsResponse::class.java, getRecommendationToolsParam)

        val reputationShopRequest = GraphqlRequest(REPUTATION_SHOPS_QUERY, ReputationShopResponse::class.java, reputationShopsParam)

        val requests = mutableListOf<GraphqlRequest>()

        val shopScoreWrapperResponse = ShopScoreWrapperResponse()

        when {
            userSession.isShopOfficialStore -> {
                requests.add(shopScoreLevelRequest)
                requests.add(shopLevelRequest)
                requests.add(getRecommendationToolsRequest)
                try {
                    val gqlResponse = gqlRepository.getReseponse(requests)

                    if (gqlResponse.getError(ShopScoreWrapperResponse::class.java).isNullOrEmpty()) {
                        val shopScoreLevelData = gqlResponse.getData<ShopScoreLevelResponse>(ShopScoreLevelResponse::class.java).shopScoreLevel
                        shopScoreWrapperResponse.shopScoreLevelResponse = shopScoreLevelData
                    } else {
                        val shopScoreLevelErrorMessage = gqlResponse.getError(ShopScoreWrapperResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(shopScoreLevelErrorMessage)
                    }

                    if (gqlResponse.getError(ShopLevelTooltipResponse::class.java).isNullOrEmpty()) {
                        val shopLevelTooltipData = gqlResponse.getData<ShopLevelTooltipResponse>(ShopLevelTooltipResponse::class.java).shopLevel
                        shopScoreWrapperResponse.shopScoreTooltipResponse = shopLevelTooltipData
                    } else {
                        val shopLevelTooltipErrorMessage = gqlResponse.getError(ShopLevelTooltipResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(shopLevelTooltipErrorMessage)
                    }

                    if (gqlResponse.getError(GetRecommendationToolsResponse::class.java).isNullOrEmpty()) {
                        val getRecommendationToolsData = gqlResponse.getData<GetRecommendationToolsResponse>(GetRecommendationToolsResponse::class.java).valuePropositionGetRecommendationTools
                        shopScoreWrapperResponse.getRecommendationToolsResponse = getRecommendationToolsData
                    } else {
                        val getRecommendationToolsErrorMessage = gqlResponse.getError(GetRecommendationToolsResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(getRecommendationToolsErrorMessage)
                    }

                } catch (e: Throwable) {
                    throw MessageErrorException(e.message)
                }
            }
            else -> {
                with(requests) {
                    add(shopScoreLevelRequest)
                    add(shopLevelRequest)
                    add(goldPMStatusRequest)
                    add(goldPMShopInfoRequest)
                    add(goldPMGradeBenefitInfoRequest)
                    add(getRecommendationToolsRequest)
                    add(reputationShopRequest)
                }

                try {
                    val gqlResponse = gqlRepository.getReseponse(requests)

                    if (gqlResponse.getError(ShopScoreWrapperResponse::class.java).isNullOrEmpty()) {
                        val shopScoreLevelData = gqlResponse.getData<ShopScoreLevelResponse>(ShopScoreLevelResponse::class.java).shopScoreLevel
                        shopScoreWrapperResponse.shopScoreLevelResponse = shopScoreLevelData
                    } else {
                        val shopScoreLevelErrorMessage = gqlResponse.getError(ShopScoreWrapperResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(shopScoreLevelErrorMessage)
                    }

                    if (gqlResponse.getError(ShopLevelTooltipResponse::class.java).isNullOrEmpty()) {
                        val shopLevelTooltipData = gqlResponse.getData<ShopLevelTooltipResponse>(ShopLevelTooltipResponse::class.java).shopLevel
                        shopScoreWrapperResponse.shopScoreTooltipResponse = shopLevelTooltipData
                    } else {
                        val shopLevelTooltipErrorMessage = gqlResponse.getError(ShopLevelTooltipResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(shopLevelTooltipErrorMessage)
                    }

                    if (gqlResponse.getError(GoldGetPMStatusResponse::class.java).isNullOrEmpty()) {
                        val goldPMStatusData = gqlResponse.getData<GoldGetPMStatusResponse>(GoldGetPMStatusResponse::class.java).goldGetPMOSStatus
                        shopScoreWrapperResponse.goldGetPMStatusResponse = goldPMStatusData
                    } else {
                        val goldPMStatusErrorMessage = gqlResponse.getError(GoldGetPMStatusResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(goldPMStatusErrorMessage)
                    }

                    if (gqlResponse.getError(GoldGetPMShopInfoResponse::class.java).isNullOrEmpty()) {
                        val goldGetPMShopInfoData = gqlResponse.getData<GoldGetPMShopInfoResponse>(GoldGetPMShopInfoResponse::class.java).goldGetPMShopInfo
                        shopScoreWrapperResponse.goldGetPMShopInfoResponse = goldGetPMShopInfoData
                    } else {
                        val goldGetPMShopInfoErrorMessage = gqlResponse.getError(GoldGetPMShopInfoResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(goldGetPMShopInfoErrorMessage)
                    }

                    if (gqlResponse.getError(GoldPMGradeBenefitInfoResponse::class.java).isNullOrEmpty()) {
                        val goldPMGradeBenefitInfoData = gqlResponse.getData<GoldPMGradeBenefitInfoResponse>(GoldPMGradeBenefitInfoResponse::class.java).goldGetPMGradeBenefitInfo
                        shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse = goldPMGradeBenefitInfoData
                    } else {
                        val goldPMGradeBenefitErrorMessage = gqlResponse.getError(GoldPMGradeBenefitInfoResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(goldPMGradeBenefitErrorMessage)
                    }

                    if (gqlResponse.getError(GetRecommendationToolsResponse::class.java).isNullOrEmpty()) {
                        val getRecommendationToolsData = gqlResponse.getData<GetRecommendationToolsResponse>(GetRecommendationToolsResponse::class.java).valuePropositionGetRecommendationTools
                        shopScoreWrapperResponse.getRecommendationToolsResponse = getRecommendationToolsData
                    } else {
                        val getRecommendationToolsErrorMessage = gqlResponse.getError(GetRecommendationToolsResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(getRecommendationToolsErrorMessage)
                    }

                    if (gqlResponse.getError(ReputationShopResponse::class.java).isNullOrEmpty()) {
                        val reputationShopData = gqlResponse.getData<ReputationShopResponse>(ReputationShopResponse::class.java).reputationShops
                        shopScoreWrapperResponse.reputationShopResponse = reputationShopData
                    } else {
                        val getReputationShopErrorMessage = gqlResponse.getError(ReputationShopResponse::class.java).joinToString(prefix = ",") { it.message }
                        throw MessageErrorException(getReputationShopErrorMessage)
                    }
                } catch (e: Throwable) {
                    throw MessageErrorException(e.message)
                }
            }
        }
        return shopScoreWrapperResponse
    }
}