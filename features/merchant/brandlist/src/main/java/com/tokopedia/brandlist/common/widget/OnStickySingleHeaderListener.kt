package com.tokopedia.brandlist.common.widget

interface OnStickySingleHeaderListener {
    fun refreshSticky()
    val isStickyShowed: Boolean
    fun setStickyChips(isNeedToSetSticcky: Boolean)
}