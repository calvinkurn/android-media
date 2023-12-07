package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetEtalaseList : GqlQueryInterface {

    private const val OPERATION_NAME = "shopShowcasesByShopID"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query getEtalaseList (${ '$' } shopId : String !){
            $OPERATION_NAME(shopId:${ '$' } shopId, hideNoCount:true, hideShowcaseGroup:true, isOwner:false) {
            result {
                id
                name
                count
                type
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
