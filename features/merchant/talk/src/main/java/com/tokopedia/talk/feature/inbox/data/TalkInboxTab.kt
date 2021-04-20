package com.tokopedia.talk.feature.inbox.data

sealed class TalkInboxTab(open val tabParam: String) {
    companion object {
        const val BUYER_TAB = "buyer"
        const val SHOP_TAB = "seller"
        const val SHOP_OLD = "auto"
    }
    data class TalkBuyerInboxTab(override val tabParam: String = BUYER_TAB): TalkInboxTab(tabParam)
    data class TalkShopInboxTab(override val tabParam: String = SHOP_OLD): TalkInboxTab(tabParam)
}