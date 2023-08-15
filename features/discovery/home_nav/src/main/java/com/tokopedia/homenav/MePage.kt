package com.tokopedia.homenav

internal annotation class MePage(val type: Widget = Widget.OTHERS) {
    enum class Widget {
        WISHLIST, REVIEW, TRANSACTION, OTHERS
    }
}
