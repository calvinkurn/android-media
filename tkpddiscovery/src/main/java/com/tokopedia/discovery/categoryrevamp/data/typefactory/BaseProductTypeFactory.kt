package com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory

interface BaseProductTypeFactory {

    fun getRecyclerViewItem(): Int

    fun setRecyclerViewItem(recyclerViewItem: Int)
}