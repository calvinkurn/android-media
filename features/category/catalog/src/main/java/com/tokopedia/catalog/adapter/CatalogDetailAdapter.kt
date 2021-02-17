package com.tokopedia.catalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel

class CatalogDetailAdapter (asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel>,
                            private val catalogListener : CatalogDetailListener,
                            private val catalogAdapterTypeFactory: CatalogDetailAdapterFactory)
    :ListAdapter<BaseCatalogDataModel, AbstractViewHolder<*>>(asyncDifferConfig){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return catalogAdapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseCatalogDataModel>, getItem(position))
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    fun bind(holder: AbstractViewHolder<BaseCatalogDataModel>, item: BaseCatalogDataModel) {
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(catalogAdapterTypeFactory) ?: HideViewHolder.LAYOUT
    }
}