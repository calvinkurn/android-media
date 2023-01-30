package com.tokopedia.dilayanitokopedia.common.base.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory

/**
 * Created by irpan on 12/09/22.
 */
open class BaseDtListAdapter<T, F : AdapterTypeFactory>(
    baseListAdapterTypeFactory: F,
    private val differ: BaseDtDiffer
) : BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

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
        if (isLoading) {
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
