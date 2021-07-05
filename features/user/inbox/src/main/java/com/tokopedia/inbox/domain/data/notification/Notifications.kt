package com.tokopedia.inbox.domain.data.notification


import com.google.gson.annotations.SerializedName

data class Notifications(
        @SerializedName("inbox_counter")
        val inboxCounter: InboxCounter = InboxCounter(),
        @SerializedName("total_cart")
        val totalCart: Int = 0
) {
        fun adjustTotalCounterBasedOn(page: Int, isShowBottomNav: Boolean) {
                inboxCounter.buyer.setCounterAdjuster(page, isShowBottomNav)
                inboxCounter.seller.setCounterAdjuster(page, isShowBottomNav)
        }
}