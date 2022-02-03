package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.SUGGESTED_REVIEW_QUERY
import com.tokopedia.home.beranda.di.module.query.QuerySuggestedReview.SUGGESTED_REVIEW_QUERY_NAME

@GqlQuery(SUGGESTED_REVIEW_QUERY_NAME, SUGGESTED_REVIEW_QUERY)
internal object QuerySuggestedReview {
    const val SUGGESTED_REVIEW_QUERY_NAME = "SuggestedReviewQuery"
    const val SUGGESTED_REVIEW_QUERY: String = "{ query suggestedProductReview() { \n" +
            "  suggestedProductReview{\n" +
            "    title\n" +
            "    description\n" +
            "    imageUrl\n" +
            "    linkURL\n" +
            "    dismissable\n" +
            "    dismissURL\n" +
            "    orderID\n" +
            "    productID\n" +
            "  }\n" +
            " }" +
            "}"
}