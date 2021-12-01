package com.tokopedia.tokopedianow.home.domain.query

internal object GetRepurchaseWidget {

    val QUERY = """
        query TokonowRepurchaseWidget(${'$'}warehouseID: String!, ${'$'}queryParam: String!) {
            TokonowRepurchaseWidget(warehouseID:${'$'}warehouseID, queryParam:${'$'}queryParam) {
                header {
                    process_time
                    messages
                    reason
                    error_code
                }
                data {
                    title
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