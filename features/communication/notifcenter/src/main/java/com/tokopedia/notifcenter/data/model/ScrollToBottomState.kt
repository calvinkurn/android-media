package com.tokopedia.notifcenter.data.model

class ScrollToBottomState {

    var lastSeenItem: Int = NOT_YET_SCROLLED
    var offset: Int = NOT_YET_SCROLLED

    fun hasScrolledDown(): Boolean {
        return lastSeenItem > offset
    }

    fun updateOffset() {
        offset = lastSeenItem
    }

    companion object {
        const val NOT_YET_SCROLLED = -1
    }
}