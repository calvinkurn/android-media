package com.tokopedia.catalogcommon.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.OnStickySingleHeaderListener

class WidgetCatalogAdapter(
    baseListAdapterTypeFactory: CatalogAdapterFactoryImpl
) : BaseCatalogAdapter<Visitable<*>, CatalogAdapterFactoryImpl>(
    baseListAdapterTypeFactory
) {

    private var recyclerView: RecyclerView? = null
    private val differ = CatalogDifferImpl()

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null

    override fun addWidget(itemList: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, itemList)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(itemList)
        result.dispatchUpdatesTo(this)
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }
}
