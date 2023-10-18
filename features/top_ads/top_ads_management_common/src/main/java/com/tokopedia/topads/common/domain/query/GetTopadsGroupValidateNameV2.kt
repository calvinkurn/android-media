package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsGroupValidateNameV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGroupValidateNameV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } shopID : String !, ${ '$' } groupName : String !, ${ '$' } source : String !){
            $OPERATION_NAME(shopID: ${ '$' } shopID, groupName: ${ '$' } groupName, source: ${ '$' } source){
            data {
                shopID
                groupName
            }
            errors {
                code
                detail
                title
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
