package com.tokopedia.home.beranda.di.module.query

object QuerySuggestedReview {
    val suggestedReviewQuery : String = "{\n" +
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
            "}"
}