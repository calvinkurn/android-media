package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsProductV3 : GqlQueryInterface {

    private const val OPERATION_NAME = "getProductV3"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } productID : String !, ${ '$' } options : OptionV3 !){
            $OPERATION_NAME(productID:${ '$' } productID, options: ${ '$' } options){
            productID
            productName
            price
            description
            category {
                id
                name
                title
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
