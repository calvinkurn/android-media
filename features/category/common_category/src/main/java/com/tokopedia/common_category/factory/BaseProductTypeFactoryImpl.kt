package com.tokopedia.common_category.factory

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

open class BaseProductTypeFactoryImpl : BaseAdapterTypeFactory(), BaseProductTypeFactory {

    private var recyclerViewItem: Int = 0


    override fun getRecyclerViewItem(): Int {
        return recyclerViewItem
    }

    override fun setRecyclerViewItem(recyclerViewItem: Int) {
        this.recyclerViewItem = recyclerViewItem
    }
}