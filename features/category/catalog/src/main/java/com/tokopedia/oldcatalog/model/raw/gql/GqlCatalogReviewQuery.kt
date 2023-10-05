package com.tokopedia.oldcatalog.model.raw.gql

const val GQL_CATALOG_REVIEW_QUERY: String = """query catalogGetProductReview(${'$'}catalog_id: String!,${'$'}key: String!, ${'$'}value: String!) {
  catalogGetProductReview(catalog_id: ${'$'}catalog_id,key: ${'$'}key, value: ${'$'}value) {
    header{
      code
      message
    }
    reviewData{
      avgRating
      totalHelpfulReview
      reviews {
        rating
        informativeScore
        reviewerName
        reviewDate
        reviewText
        reviewImageUrl
        reviewId
        productUrl
      }
    }
  }
}
"""
