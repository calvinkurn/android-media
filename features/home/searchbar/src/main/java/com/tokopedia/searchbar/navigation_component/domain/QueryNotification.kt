package com.tokopedia.searchbar.navigation_component.domain

object QueryNotification {
    val query: String = "" +
            "query Notification {\n" +
            "                  status\n" +
            "                  userShopInfo {\n" +
            "                    info {\n" +
            "                      shop_id\n" +
            "                    }\n" +
            "                  }\n" +
            "\n" +
            "                  notifications(){\n" +
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
}