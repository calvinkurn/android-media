package com.tokopedia.shop.score.domain.query

internal object GetShopScore {
    val QUERY = """
        query shopScore(${'$'}input:ShopScoreParam!) {
          shopScore(input:${'$'}input) {
            result {
              shopScoreSummary {
                title
                value
                color
              }
              shopScoreDetail {
                title
                value
                maxValue
                color
                description
              }
            }
            error {
                message
            }
          }
        }
    """.trimIndent()
}