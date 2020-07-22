package com.tokopedia.shop.common.data.source.cloud.query

internal object GetShopFreeShippingInfo {

    private const val shopIDs = "\$shopIDs"

    val QUERY = """
        query shopInfoByID($shopIDs:[Int!]!){
            shopInfoByID(input: {
                shopIDs:$shopIDs,
                fields:["shipment"]
            }){
                result {
                    freeOngkir{
                        isActive
                        imgURL
                    }
                }
            }
        }
    """.trimIndent()
}