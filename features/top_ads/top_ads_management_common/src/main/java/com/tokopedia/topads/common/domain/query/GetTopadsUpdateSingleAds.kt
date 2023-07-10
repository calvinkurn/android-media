package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsUpdateSingleAds : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsUpdateSingleAds"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        mutation $OPERATION_NAME (${ '$' } action : String !, ${ '$' } ads :[topadsUpdateSingleAdsReqData]!, ${ '$' } shopID : String !) {
            $OPERATION_NAME(action: ${ '$' } action, ads: ${ '$' } ads, shopID: ${ '$' } shopID) {
            data {
                action
                shopID
            }
            errors {
                code
                detail
                title
            }
        }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
