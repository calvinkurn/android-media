package com.tokopedia.tokomart.common.listener

interface OnStickySingleHeaderListener {
    fun refreshSticky()
    val isStickyShowed: Boolean
}