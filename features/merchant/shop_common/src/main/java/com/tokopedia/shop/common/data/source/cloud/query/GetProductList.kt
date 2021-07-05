package com.tokopedia.shop.common.data.source.cloud.query

internal object GetProductList {

    val QUERY = """
       query ProductList(${'$'}shopID:String!, ${'$'}warehouseID:String, ${'$'}filter:[GoodsFilterInput], ${'$'}sort:GoodsSortInput, ${'$'}extraInfo:[String]){
        ProductList(shopID:${'$'}shopID, warehouseID:${'$'}warehouseID, filter:${'$'}filter, sort:${'$'}sort, extraInfo:${'$'}extraInfo){
            header{
                processTime
                messages
                reason
                errorCode
            }
            data{
                id
                name
                price{
                    min
                    max
                }
                stock
                stockReserved
                hasStockReserved
                isCampaign
                cashback
                status
                featured
                isVariant
                url
                sku
                pictures{
                    urlThumbnail
                }
                topads {
                    status
                    management
                }
            }
        }
    }
    """.trimIndent()
}