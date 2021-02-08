package com.tokopedia.catalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel

class CatalogDetailAdapter (asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel>,
                            private val listener : CatalogDetailListener,
                            private val catalogAdapterTypeFactory: AdapterTypeFactory)
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

}