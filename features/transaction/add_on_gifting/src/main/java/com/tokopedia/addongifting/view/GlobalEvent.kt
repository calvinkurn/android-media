package com.tokopedia.addongifting.view

data class GlobalEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val STATE_FAILED_LOAD_ADD_ON_DATA = 11
        const val STATE_SHOW_CLOSE_DIALOG_CONFIRMATION = 21
        const val STATE_DISMISS_BOTTOM_SHEET = 31
    }
}