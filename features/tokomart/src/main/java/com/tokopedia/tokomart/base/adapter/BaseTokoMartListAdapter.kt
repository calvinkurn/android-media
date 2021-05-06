package com.tokopedia.tokomart.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory

abstract class BaseTokoMartListAdapter<T, F: AdapterTypeFactory>(
    baseListAdapterTypeFactory: F,
    private val differ: BaseTokoMartDiffer
): BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

    fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    override fun showLoading() {
        if (!isLoading) {
            val items = getItems()
            if (isShowLoadingMore) {
                items.add(loadingMoreModel)
            } else {
                items.add(loadingModel)
            }
            submitList(items)
        }
    }

    override fun hideLoading() {
        if(isLoading) {
            val items = getItems()
            items.removeAt(lastIndex)
            submitList(items)
        }
    }

    override fun removeErrorNetwork() {
        val items = getItems()
        items.remove(errorNetworkModel)
        submitList(items)
    }

    override fun clearAllElements() {
        submitList(emptyList())
    }

    private fun getItems(): MutableList<Visitable<*>> {
        return visitables.toMutableList()
    }
}
