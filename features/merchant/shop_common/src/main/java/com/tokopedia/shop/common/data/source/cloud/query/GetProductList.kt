package com.tokopedia.shop.common.data.source.cloud.query

internal object GetProductList {

    val QUERY = """
       query ProductList(${'$'}shopID:String!, ${'$'}warehouseID:String, ${'$'}filter:[GoodsFilterInput], ${'$'}sort:GoodsSortInput, ${'$'}extraInfo:[String], ${'$'}pageSource:String!){
        ProductList(shopID:${'$'}shopID, warehouseID:${'$'}warehouseID, filter:${'$'}filter, sort:${'$'}sort, extraInfo:${'$'}extraInfo, pageSource:${'$'}pageSource){
            header{
                processTime
                messages
                reason
                errorCode
            }
            meta {
                totalHits
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
                campaignType {
                    id
                    name
                    iconURL
                }
                suspendLevel
                hasStockAlert
                stockAlertActive
                stockAlertCount
                haveNotifyMeOOS
                notifyMeOOSCount
                notifyMeOOSWording
                isEmptyStock
                manageProductData {
                   isStockGuaranteed
                   isTobacco
                   isDTInbound
                   isArchived
                   isInGracePeriod
                }
            }
        }
    }
    """.trimIndent()
}
