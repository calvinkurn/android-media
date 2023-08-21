package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsPostAutoAdsV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsPostAutoAdsV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """mutation $OPERATION_NAME (${ '$' } input : TopAdsPostAutoAdsParamV2 !) {
            $OPERATION_NAME(input: ${ '$' } input) {
            data {
                shopID
                status
                statusDesc
                dailyUsage
                dailyBudget
                info {
                    reason
                    message
                }
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
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
