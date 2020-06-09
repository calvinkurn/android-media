package com.tokopedia.shop.common.data.source.cloud.query

internal object GetShopFreeShippingStatus {

    private const val userID = "\$userID"
    private const val shopIDs = "\$shopIDs"

    val QUERY = """
        query EligibleServiceShop($userID: Int!, $shopIDs: [Int!]!){
            EligibleServiceShop(input: {
                "userID": $userID,
                "shopIDs": $shopIDs
            }) {
                shops{
                    shopID
                    status
                    statusEligible
                    statusSellerApproval
                }
            }
        }
    """.trimIndent()
}