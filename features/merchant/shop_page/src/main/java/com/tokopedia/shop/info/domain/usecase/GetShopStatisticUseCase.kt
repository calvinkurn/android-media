package com.tokopedia.shop.info.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.info.data.model.ProductShopPackSpeed
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopRatingStats
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopSatisfaction
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopStatisticUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopStatisticsResp>() {

    var params = mapOf<String, Int>()
    var isFromCacheFirst: Boolean = true


    override suspend fun executeOnBackground(): ShopStatisticsResp {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlRequestShopPackSpeed = GraphqlRequest(QUERY_SHOP_PACK_SPEED,
                ProductShopPackSpeed.Response::class.java, params)
        val gqlRequestShopRating = GraphqlRequest(QUERY_SHOP_RATING,
                ShopRatingStats.Response::class.java, params)
        val gqlRequestShopSatisfaction = GraphqlRequest(QUERY_SHOP_SATISFACTION,
                ShopSatisfaction.Response::class.java, params)

        gqlUseCase.addRequests(listOf(gqlRequestShopPackSpeed,
                gqlRequestShopRating, gqlRequestShopSatisfaction))


        val gqlResponse = gqlUseCase.executeOnBackground()

        val shopPackSpeed = if (gqlResponse.getError(ProductShopPackSpeed.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ProductShopPackSpeed.Response>(ProductShopPackSpeed.Response::class.java).productShopPackSpeed
        } else null

        val shopRating = if (gqlResponse.getError(ShopRatingStats.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ShopRatingStats.Response>(ShopRatingStats.Response::class.java).shopRatingStats
        } else null

        val shopSatisfaction = if (gqlResponse.getError(ShopSatisfaction.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ShopSatisfaction.Response>(ShopSatisfaction.Response::class.java).shopSatisfaction
        } else null

        return ShopStatisticsResp(null, shopPackSpeed, shopRating, shopSatisfaction)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val QUERY_SHOP_PACK_SPEED = """
            query GetShopPackProcess(${'$'}shopId: Int!){
                ProductShopPackSpeedQuery(shopId: ${'$'}shopId){
                    hour
                    day
                    totalOrder
                    speedFmt
                    classification{
                        demand{
                            hour
                            day
                            speedFmt
                            totalOrder
                        }
                        regular{
                            hour
                            day
                            speedFmt
                            totalOrder
                        }
                    }
                }
            }
        """
        private const val QUERY_SHOP_SATISFACTION = """
            query getShopSatisfaction(${'$'}{'${'$'}'}shopId: Int!){
                ShopSatisfactionQuery(shopId: ${'$'}{'${'$'}'}shopId){
                    recentOneYear{
                      good
                      neutral
                      bad
                    }
                    recentSixMonth{
                      good
                      neutral
                      bad
                    }
                    recentOneMonth{
                      good
                      neutral
                      bad
                    }
                  }
            }
        """
        private const val QUERY_SHOP_RATING =  """
            query GetShopRating(${'$'}{'${'$'}'}{'${'$'}{'${'$'}'}'}shopId: Int!){
              ShopRatingQuery(shopId: ${'$'}{'${'$'}'}{'${'$'}{'${'$'}'}'}shopId) {
                ratingScore
                starLevel
                totalReview
                detail {
                  fiveStar {
                    rate
                    totalReview
                    percentageWord
                    percentage
                  }
                  fourStar {
                    rate
                    totalReview
                    percentageWord
                    percentage
                  }
                  threeStar {
                    rate
                    totalReview
                    percentageWord
                    percentage
                  }
                  twoStar {
                    rate
                    totalReview
                    percentageWord
                    percentage
                  }
                  oneStar {
                    rate
                    totalReview
                    percentageWord
                    percentage
                  }
                }
              }
            }
        """
        @JvmStatic
        fun createParams(shopId: Int): Map<String, Int> = mapOf(PARAM_SHOP_ID to shopId)
    }
}