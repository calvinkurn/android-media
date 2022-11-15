package com.tokopedia.catalog_library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel

class CatalogLibraryAdapter(
    asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel>,
    private val catalogHomepageAdapterFactory: CatalogHomepageAdapterFactory
) : ListAdapter<BaseCatalogLibraryDataModel, AbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return catalogHomepageAdapterFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseCatalogLibraryDataModel>, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(catalogHomepageAdapterFactory) ?: HideViewHolder.LAYOUT
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(holder: AbstractViewHolder<BaseCatalogLibraryDataModel>, item: BaseCatalogLibraryDataModel) {
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size
}
