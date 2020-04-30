package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

data class NotificationSellerOrderStatusUiModel(
        val arriveAtDestination: Int = 0,
        val newOrder: Int = 0,
        val readyToShip: Int = 0,
        val shipped: Int = 0
)