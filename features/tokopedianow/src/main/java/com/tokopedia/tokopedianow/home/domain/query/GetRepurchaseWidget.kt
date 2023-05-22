package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetRepurchaseWidget : GqlQueryInterface {

    private const val OPERATION_NAME = "TokonowRepurchaseWidget"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}warehouses: [WarehousePerService!], ${'$'}queryParam: String!) {
            $OPERATION_NAME(warehouses:${'$'}warehouses, queryParam:${'$'}queryParam) {
                header {
                    process_time
                    messages
                    reason
                    error_code
                }
                data {
                    title
                    subtitle
                    subtitleColor
                    listProduct {
                        id
                        name
                        url
                        imageUrl
                        price
                        slashedPrice
                        discountPercentage
                        parentProductId
                        rating
                        ratingAverage
                        countReview
                        minOrder
                        maxOrder
                        stock
                        category
                        labelGroup{
                            position
                            title
                            type
                            url
                        }
                        labelGroupVariant{
                            title
                            type
                            typeVariant
                            hexColor
                        }
                        shop{
                            id
                        }
                    }
                }
            }
       }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
