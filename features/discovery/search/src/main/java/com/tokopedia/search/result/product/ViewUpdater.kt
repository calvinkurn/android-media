package com.tokopedia.search.result.product

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface ViewUpdater {
    val itemCount: Int
    val itemList: List<Visitable<*>>?
    fun getItemAtIndex(index: Int): Visitable<*>?
    fun setItems(list: List<Visitable<*>>)
    fun appendItems(list: List<Visitable<*>>)
    fun refreshItemAtIndex(index: Int)
    fun addLoading()
    fun removeLoading()
    fun removeFirstItemWithCondition(condition: (Visitable<*>) -> Boolean)
    fun insertItemAfter(item: Visitable<*>, previousItem: Visitable<*>)
    fun requestRelayout()
    fun backToTop()
    fun scrollToPosition(position: Int)
}
