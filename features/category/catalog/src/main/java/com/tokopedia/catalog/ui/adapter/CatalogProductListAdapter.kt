package com.tokopedia.catalog.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class CatalogProductListAdapter(
    baseListAdapterTypeFactory: CatalogProductListAdapterFactoryImpl
) : BaseListAdapter<Visitable<*>, CatalogProductListAdapterFactoryImpl>(
    baseListAdapterTypeFactory
) {

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
            items.lastIndex.takeIf { it != RecyclerView.NO_POSITION }?.let { lastIndex ->
                items.removeAt(lastIndex)
                submitList(items)
            }
        }
    }

    override fun removeErrorNetwork() {
        val items = getItems()
        if (items.remove(errorNetworkModel)) {
            submitList(items)
        }
    }

    override fun addElement(itemList: MutableList<out Visitable<Any>>) {
        val items = getItems()
        items.addAll(itemList?.toList().orEmpty())
        submitList(items)
    }

    override fun clearAllElements() {
        submitList(emptyList())
    }

    @SuppressLint("NotifyDataSetChanged") // will improve this using diffutil
    private fun submitList(items: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(items)
        notifyDataSetChanged()
    }

    private fun getItems(): MutableList<Visitable<*>> {
        return visitables.toMutableList()
    }

}
