package com.tokopedia.common_category.factory

interface BaseProductTypeFactory {

    fun getRecyclerViewItem(): Int

    fun setRecyclerViewItem(recyclerViewItem: Int)
}