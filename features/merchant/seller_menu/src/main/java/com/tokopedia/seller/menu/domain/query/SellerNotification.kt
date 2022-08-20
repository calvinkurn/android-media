package com.tokopedia.seller.menu.domain.query

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller.menu.domain.query.SellerMenuNotification.PARAM_SHOP_ID

internal object SellerMenuNotification {
    private const val PARAM_INPUT = "input"
    const val PARAM_SHOP_ID = "shop_id"
    val QUERY = """
        query SellerMenuNotification($$PARAM_INPUT: NotificationRequest) {
            notifications($PARAM_INPUT: $$PARAM_INPUT){
                sellerOrderStatus {
                    newOrder
     	            readyToShip
                    inResolution
                }
                notifcenter_total_unread {
                    notif_total_unread_seller_int
                }
                inbox {
                    talk
                }
            }
        }
    """.trimIndent()

    fun getNotificationParam(shopId: String): Map<String, Any> {
        return mapOf(
            PARAM_INPUT to Param(shopId)
        )
    }
}

data class Param(
    @SerializedName(PARAM_SHOP_ID)
    val shopId: String
)