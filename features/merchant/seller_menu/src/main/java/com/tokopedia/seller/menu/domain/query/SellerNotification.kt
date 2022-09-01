package com.tokopedia.seller.menu.domain.query

import com.tokopedia.seller.menu.common.domain.entity.Param

internal object SellerMenuNotification {
    private const val PARAM_INPUT = "input"
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