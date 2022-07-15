package com.tokopedia.search.result.product

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface ViewUpdater {

    fun setItems(list: List<Visitable<*>>)
    fun appendItems(list: List<Visitable<*>>)
    fun refreshItemAtIndex(index: Int)
    fun addLoading()
    fun removeLoading()
}
