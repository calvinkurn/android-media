package com.tokopedia.review.feature.bulkreview

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetBulkReviewRecommendationWidgetQuery : GqlQueryInterface {
    private const val OPERATION_NAME = "GetBulkReviewRecommendationWidget"
    private const val PARAM_USER_ID = "userID"

    private const val QUERY = """
        query $OPERATION_NAME(
            $$PARAM_USER_ID: String!
        ) {
            productrevGetBulkReviewRecommendationWidget(
                $PARAM_USER_ID: $$PARAM_USER_ID
            ) {
                title
                list {
                    product {
                        imageURL
                    }
                }
                productCount
                productCountFmt
                linkDetail {
                    appLink
                }
            }
        }
    """

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY.trimIndent()

    override fun getTopOperationName(): String = OPERATION_NAME

    fun createParams(
        userId: String
    ) = mapOf(
        PARAM_USER_ID to userId
    )
}
