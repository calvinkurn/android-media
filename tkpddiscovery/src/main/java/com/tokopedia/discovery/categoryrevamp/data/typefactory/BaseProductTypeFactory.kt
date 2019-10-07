package com.tokopedia.discovery.categoryrevamp.data.typefactory

interface BaseProductTypeFactory {

    fun getRecyclerViewItem(): Int

    fun setRecyclerViewItem(recyclerViewItem: Int)
}