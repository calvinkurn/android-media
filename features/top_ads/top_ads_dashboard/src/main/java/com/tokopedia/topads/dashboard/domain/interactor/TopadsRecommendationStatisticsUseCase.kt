package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val query = """
            query topadsWidgetRecommendationStatistics(${'$'}shopID: String!){
              topadsWidgetRecommendationStatistics(shopID:${'$'}shopID) {
                header {
                  process_time
                } 
              	data {
                  ProductRecommmandationStats {
                    Count
                    TotalSearchCount
                    ProductList {
                      ImageURL
                    }
                  }
                  DailyBudgetRecommandationStats {
                    Count
                    TotalClicks
                    GroupList {
                      GroupName
                    }
                  }
                  KeywordRecommendationStats {
                    InsightCount
                    GroupCount
                    TopGroups {
                      GroupName
                      NewKeywordCount
                      NewKeywordTotalImpression
                      BidCount
                      BidTotalImpression
                      NegativeKeywordCount
                      NegativeKeywordPotentialSave
                    }
                  }
                  
                }
                errors {
                  code
                  detail
                  title
                  object {
                    type
                    text
                  }
                }
              }
            }
        """

@GqlQuery("TopadsWidgetRecommendationStatistics", query)
class TopadsRecommendationStatisticsUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
) {

    suspend fun fetchRecommendationStatistics(): RecommendationStatistics? {
        return Utils.executeQuery(TopadsWidgetRecommendationStatistics.GQL_QUERY, RecommendationStatistics::class.java, createParams())
    }

    private fun createParams() = mapOf(
        TopAdsDashboardConstant.PARAM_SHOP_ID to userSession.shopId
    )
}