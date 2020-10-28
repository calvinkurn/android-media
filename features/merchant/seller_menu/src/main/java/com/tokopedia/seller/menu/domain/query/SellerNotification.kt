package com.tokopedia.seller.menu.domain.query

internal object SellerMenuNotification {

    val QUERY = """
        query SellerMenuNotification() {
            notifications {
                sellerOrderStatus {
                    newOrder
     	            readyToShip
                }
                notifcenter_total_unread {
                    notif_total_unread_seller
                }
                inbox {
                    talk
                }
            }
        }
    """.trimIndent()
}