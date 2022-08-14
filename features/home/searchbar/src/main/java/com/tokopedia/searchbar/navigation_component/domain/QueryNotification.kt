package com.tokopedia.searchbar.navigation_component.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.searchbar.navigation_component.domain.QueryNotification.PARAM_SHOP_ID

object QueryNotification {
    private const val PARAM_INPUT = "input"
    const val PARAM_SHOP_ID = "shop_id"
    val query: String = "" +
            "query Notification($$PARAM_INPUT: NotificationRequest) {\n" +
            "                  status\n" +
            "                  userShopInfo {\n" +
            "                    info {\n" +
            "                      shop_id\n" +
            "                    }\n" +
            "                  }\n" +
            "\n" +
            "                  notifications($PARAM_INPUT: $$PARAM_INPUT){\n" +
            "                  \ttotal_cart\n" +
            "                    inbox {\n" +
            "                      talk\n" +
            "                      ticket\n" +
            "                      review\n" +
            "                    }\n" +
            "                    chat {\n" +
            "                      unreads\n" +
            "                      unreadsSeller\n" +
            "                      unreadsUser\n" +
            "                    }\n" +
            "                    inbox_counter{\n" +
            "                      all{\n" +
            "                        total_int\n" +
            "                        notifcenter_int\n" +
            "                      }\n" +
            "                    }\n" +
            "                    resolutionAs {\n" +
            "                      buyer\n" +
            "                      seller\n" +
            "                    }\n" +
            "                    sellerInfo {\n" +
            "                      notification\n" +
            "                    }\n" +
            "                    buyerOrderStatus {\n" +
            "                      paymentStatus\n" +
            "                      confirmed\n" +
            "                      processed\n" +
            "                      shipped\n" +
            "                      arriveAtDestination\n" +
            "                    }\n" +
            "                    sellerOrderStatus {\n" +
            "                      newOrder\n" +
            "                      readyToShip\n" +
            "                      shipped\n" +
            "                      arriveAtDestination\n" +
            "                      inResolution\n" +
            "                    }\n" +
            "                  }\n" +
            "\n" +
            "                  notifcenter_unread {\n" +
            "                    notif_unread\n" +
            "                  }\n" +
            "                }"

    fun getNotificationParam(shopId: String): Map<String, Any> {
        return mapOf(
            PARAM_INPUT to Param(shopId.toLongOrZero())
        )
    }
}

data class Param(
    @SerializedName(PARAM_SHOP_ID)
    var shopId: Long
)