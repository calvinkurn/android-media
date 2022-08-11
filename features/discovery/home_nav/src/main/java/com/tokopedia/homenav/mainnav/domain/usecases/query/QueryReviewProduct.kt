package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryReviewProduct.PRODUCT_WAIT_WAIT_FEEDBACK_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryReviewProduct.PRODUCT_WAIT_WAIT_FEEDBACK_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(PRODUCT_WAIT_WAIT_FEEDBACK_QUERY_NAME, PRODUCT_WAIT_WAIT_FEEDBACK_QUERY)
object QueryReviewProduct {
    const val PRODUCT_WAIT_WAIT_FEEDBACK_QUERY_NAME = "ProductRevWaitForFeedbackQuery"
    const val PRODUCT_WAIT_WAIT_FEEDBACK_QUERY = "" +
            "query productrevWaitForFeedback(\$limit: Int!, \$page: Int!) {\n" +
            "  productrevWaitForFeedback(limit: \$limit, page: \$page) {" +
            "    hasNext\n" +
            "    list {\n" +
            "      reputationIDStr\n" +
            "      product {\n" +
            "        productIDStr\n" +
            "        productName\n" +
            "        productImageURL\n" +
            "      }\n" +
            "      timestamp {\n" +
            "        createTime\n" +
            "        createTimeFormatted\n" +
            "      }\n" +
            "      status {\n" +
            "        seen\n" +
            "        isEligible\n" +
            "        incentiveLabel\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}