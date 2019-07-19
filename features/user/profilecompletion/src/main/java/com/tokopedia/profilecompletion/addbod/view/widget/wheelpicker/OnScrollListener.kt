package com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker

/**
 * Created by Ade Fulki on 2019-07-18.
 * ade.hadian@tokopedia.com
 */

interface OnScrollListener {

    fun onScrollStateChange(scrollState: Int)

    companion object {
        const val SCROLL_STATE_IDLE = 0
        const val SCROLL_STATE_TOUCH_SCROLL = 1
        const val SCROLL_STATE_FLING = 2
    }
}