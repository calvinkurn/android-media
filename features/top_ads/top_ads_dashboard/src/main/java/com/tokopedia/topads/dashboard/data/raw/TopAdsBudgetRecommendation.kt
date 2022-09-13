package com.tokopedia.topads.dashboard.data.raw

const val BUDGET_RECOMMENDATION = """
    query topadsGetDailyBudgetRecommendationV2(${'$'}shopId: String!) {
      topadsGetDailyBudgetRecommendationV2(shopId:${'$'}shopId    ) {
        data {
          group_id
          group_name
          price_daily
          suggested_price_daily
          avg_bid
        }
        errors {
          code
          detail
          object {
            type
            text
          }
          title
        }
      }
    }
"""