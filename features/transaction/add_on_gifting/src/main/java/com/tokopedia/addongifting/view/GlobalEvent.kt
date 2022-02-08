package com.tokopedia.addongifting.view

data class GlobalEvent(
        var state: Int = 0,
        var data: Any? = null,
        var throwable: Throwable? = null
) {
    companion object {
        const val STATE_FAILED_LOAD_ADD_ON_DATA = 11

    }
}