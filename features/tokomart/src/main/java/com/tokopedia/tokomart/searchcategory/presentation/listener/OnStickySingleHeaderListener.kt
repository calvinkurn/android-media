package com.tokopedia.tokomart.searchcategory.presentation.listener

interface OnStickySingleHeaderListener {
    fun refreshSticky()
    val isStickyShowed: Boolean
}