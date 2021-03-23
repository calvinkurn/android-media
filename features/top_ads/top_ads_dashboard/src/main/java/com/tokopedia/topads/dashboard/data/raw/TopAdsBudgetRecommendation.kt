package com.tokopedia.topads.dashboard.data.raw

const val BUDGET_RECOMMENDATION = """
    query topadsGetDailyBudgetRecommendation(${'$'}shopId: Int!) {
      topadsGetDailyBudgetRecommendation(shopId:${'$'}shopId    ) {
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