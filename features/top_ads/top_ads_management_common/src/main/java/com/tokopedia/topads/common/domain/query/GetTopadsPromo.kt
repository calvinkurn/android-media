package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsPromo : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGetPromo"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } shopID : String !, ${ '$' } adID : String !) {
            $OPERATION_NAME(shopID: ${ '$' } shopID, adID: ${ '$' } adID) {
            data {
                adID
                adType
                groupID
                shopID
                itemID
                status
                priceBid
                priceDaily
                adStartDate
                adStartTime
                adEndDate
                adEndTime
                adImage
                adTitle
                cpmDetails {
                    link
                    title
                    description {
                        slogan
                    }
                    product {
                        productID
                        productName
                        productImage
                        productURL
                        productPrice
                        productActive
                        productRating
                        productReviewCount
                        departmentID
                        departmentName
                    }
                }
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
