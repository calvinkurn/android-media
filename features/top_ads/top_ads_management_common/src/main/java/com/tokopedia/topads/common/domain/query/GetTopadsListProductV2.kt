package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsListProductV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsGetListProductV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } shopID : String !, ${ '$' } keyword : String,
        ${ '$' } etalase : String, ${ '$' } sortBy : String, ${ '$' } rows : Int !, ${ '$' } start : Int, ${ '$' } status : String, ${ '$' } source : String !)
        {
            $OPERATION_NAME(shopID: ${ '$' } shopID, keyword: ${ '$' } keyword, etalase: ${ '$' } etalase, sortBy: ${ '$' } sortBy,
            rows: ${ '$' } rows, start: ${ '$' } start, status:${ '$' } status, source:${ '$' } source) {
            data {
                productID
                productName
                productPrice
                productPriceNum
                productImage
                productIsPromoted
                productURI
                adID
                adStatus
                groupName
                groupID
                departmentID
                departmentName
                priceBid
                suggestedBid
                productRating
                productReviewCount
            }
            eof
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
