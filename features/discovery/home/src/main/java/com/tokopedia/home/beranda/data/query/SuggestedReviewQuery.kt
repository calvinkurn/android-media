package com.tokopedia.home.beranda.data.query

object SuggestedReviewQuery {
    val suggestedReviewQuery = """
        {
          suggestedProductReview{
            title
            description
            imageUrl
            linkURL
            dismissable
            dismissURL
            orderID
            productID
          }
        }
    """.trimIndent()

    val dismissSuggestedReviewQuery = """
        query productrevDismissSuggestion{
          productrevDismissSuggestion
        }
    """.trimIndent()
}