package com.tokopedia.shop.score.domain.query

internal object GetShopScore {
    val QUERY = """
        query shopScore(${'$'}input:ShopScoreParam!) {
          shopScore(input:${'$'}input) {
            result {
              shopID
              shopScore
              shopScoreSummary {
                title
                value
                maxValue
                color
                description
              }
              shopScoreDetail {
                title
                value
                maxValue
                color
                description
              }
              badgeScore
            }
            error {
                message
            }
          }
        }
    """.trimIndent()
}