package com.tokopedia.reviewseller.feature.reviewdetail.util

object GqlQueryDetail {
    val GET_PRODUCT_FEEDBACK_LIST_DETAIL = """
        query get_product_feedback_detail(${'$'}productID: Int!, ${'$'}sortBy: String!, ${'$'}filterBy: String!, ${'$'}limit: Int!, ${'$'}page: Int!) {
            productrevFeedbackDataPerProduct(productID: ${'$'}productID, sortBy: ${'$'}sortBy, filterBy: ${'$'}filterBy, limit: ${'$'}limit, page: ${'$'}page) {
                list {
                    feedbackID
                    rating
                    reviewText
                    reviewTime
                    autoReply
                    replyText
                    replyTime
                    attachments {
                        thumbnailURL
                        fullsizeURL
                    }
                    variantName
                    reviewerName
                }
                topics {
                    title
                    count
                    formatted
                }
                aggregatedRating {
                    rating
                    ratingCount
                }
                sortBy
                filterBy
                limit
                page
                hasNext
                reviewCount
            }
        }
        """.trimIndent()

    val GET_PRODUCT_REVIEW_DETAIL_OVERALL = """
        query get_product_review_detail_overall(${'$'}productID: Int!, ${'$'}filterBy: String!) {
             productrevGetReviewAggregateByProduct(productID: ${'$'}productID, filterBy: ${'$'}filterBy) {
               productName
               ratingAverage
               ratingCount
               period
            }
        }
        """.trimIndent()

    val GQL_GET_PRODUCT_FEEDBACK_FILTER = """
        query get_product_feedback_detail(${'$'}productID: Int!, ${'$'}sortBy: String!, ${'$'}filterBy: String!, ${'$'}limit: Int!, ${'$'}page: Int!) {
          productrevFeedbackDataPerProduct(productID: ${'$'}productID, sortBy: ${'$'}sortBy,
            filterBy: ${'$'}filterBy, limit: ${'$'}limit, page: ${'$'}page) {
              topics {
                  title
                  count
                  formatted
              }
              aggregatedRating {
                  rating
                  ratingCount
              }
              reviewCount
            }
        }
        """.trimIndent()
}