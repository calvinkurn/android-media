package com.tokopedia.shop.common.data.source.cloud.query

internal object GetProductList {

    val QUERY = """
       query ProductList(${'$'}shopID:String!, ${'$'}filter:[GoodsFilterInput], ${'$'}sort:GoodsSortInput, ${'$'}extraInfo:[String]){
        ProductList(shopID:${'$'}shopID, filter:${'$'}filter, sort:${'$'}sort, extraInfo:${'$'}extraInfo){
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
                topads {
                    status
                    management
                }
            }
        }
    }
    """.trimIndent()

    val QUERY_BROADCAST_CHAT = """
        query {
          chatBlastSellerMetadata {
            shopId
            promo
            promoType
            expirePromo
            url
          }
        }
    """.trimIndent()
}