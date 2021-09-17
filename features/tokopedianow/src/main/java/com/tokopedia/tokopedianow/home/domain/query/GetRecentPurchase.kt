package com.tokopedia.tokopedianow.home.domain.query

internal object GetRecentPurchase {

    val QUERY = """
        query TokonowRepurchaseWidget(${'$'}warehouseID: String!) {
            TokonowRepurchaseWidget(warehouseID:${'$'}warehouseID) {
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