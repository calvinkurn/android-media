package com.tokopedia.shop.score.performance.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.score.performance.domain.model.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetShopPerformanceUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
        UseCase<ShopScoreWrapperResponse>() {

    companion object {
        const val SHOP_ID_STATUS_INFO = "shopID"
        const val SHOP_ID_BENEFIT_INFO = "shop_id"
        const val SHOP_SCORE_LEVEL_INPUT = "input"
        const val SHOP_SCORE_TOOLTIP_KEY = "tooltip_key"

        val GOLD_PM_SHOP_INFO_QUERY = """
            query goldGetPMShopInfo(${'$'}shop_id: Int!) {
              goldGetPMShopInfo(shop_id:${'$'}shop_id, source: "android-shop-score", lang: "id", device: "android") {
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
              valuePropositionGetRecommendationTools(shopID: ${'$'}shopID, source:"android-shopscore-ui") {
                 recommendation_tools {
                  title
                  text
                  image_url
                  related_link_applink
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

        val goldPMShopInfoParam = mapOf(SHOP_ID_BENEFIT_INFO to shopID)

        val getRecommendationToolsParam = mapOf(SHOP_ID_STATUS_INFO to shopID)

        val shopScoreLevelRequest = GraphqlRequest(SHOP_SCORE_LEVEL_QUERY, ShopScoreLevelResponse::class.java, shopScoreLevelParam)
        val shopLevelRequest = GraphqlRequest(SHOP_LEVEL_TOOLTIP_QUERY, ShopLevelTooltipResponse::class.java, shopLevelParam)
        val goldPMShopInfoRequest = GraphqlRequest(GOLD_PM_SHOP_INFO_QUERY, GoldGetPMShopInfoResponse::class.java, goldPMShopInfoParam)

        val getRecommendationToolsRequest =
                GraphqlRequest(RECOMMENDATION_TOOLS_QUERY, GetRecommendationToolsResponse::class.java, getRecommendationToolsParam)

        val requests = mutableListOf<GraphqlRequest>()

        val shopScoreWrapperResponse = ShopScoreWrapperResponse()

        with(requests) {
            add(shopScoreLevelRequest)
            add(shopLevelRequest)
            add(goldPMShopInfoRequest)
            add(getRecommendationToolsRequest)
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
            }

            if (gqlResponse.getError(GoldGetPMShopInfoResponse::class.java).isNullOrEmpty()) {
                val goldGetPMShopInfoData = gqlResponse.getData<GoldGetPMShopInfoResponse>(GoldGetPMShopInfoResponse::class.java).goldGetPMShopInfo
                shopScoreWrapperResponse.goldGetPMShopInfoResponse = goldGetPMShopInfoData
            }

            if (gqlResponse.getError(GetRecommendationToolsResponse::class.java).isNullOrEmpty()) {
                val getRecommendationToolsData = gqlResponse.getData<GetRecommendationToolsResponse>(GetRecommendationToolsResponse::class.java).valuePropositionGetRecommendationTools
                shopScoreWrapperResponse.getRecommendationToolsResponse = getRecommendationToolsData
            }

        } catch (e: Throwable) {
        }

        return shopScoreWrapperResponse
    }
}