package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetEtalaseList : GqlQueryInterface {

    private const val OPERATIOIN_NAME = "shopShowcasesByShopID"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATIOIN_NAME)
    }

    override fun getQuery(): String {
        return """query getEtalaseList (${ '$' } shopId : String !){
            $OPERATIOIN_NAME(shopId:${ '$' } shopId, hideNoCount:true, hideShowcaseGroup:true, isOwner:false) {
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
        return OPERATIOIN_NAME
    }
}
