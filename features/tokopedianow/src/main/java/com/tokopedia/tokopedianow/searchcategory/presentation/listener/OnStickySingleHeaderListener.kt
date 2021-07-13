package com.tokopedia.tokopedianow.searchcategory.presentation.listener

interface OnStickySingleHeaderListener {
    fun refreshSticky()
    val isStickyShowed: Boolean
}