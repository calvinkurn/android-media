package com.tokopedia.shop.common.data.source.cloud.query

internal object GetProductList {

    val QUERY = """
       query ProductList(${'$'}shopID:String!, ${'$'}filter:[GoodsFilterInput], ${'$'}sort:GoodsSortInput){
        ProductList(shopID:${'$'}shopID, filter:${'$'}filter, sort:${'$'}sort){
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
                cashback
                status
                featured
                isVariant
                url
                sku
                pictures{
                    urlThumbnail
                }
            }
        }
    }
    """.trimIndent()
}