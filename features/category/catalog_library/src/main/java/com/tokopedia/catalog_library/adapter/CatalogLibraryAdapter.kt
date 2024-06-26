package com.tokopedia.catalog_library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.viewholder.CatalogLibraryAbstractViewHolder

class CatalogLibraryAdapter(
    asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM>,
    private val catalogHomepageAdapterFactory: CatalogHomepageAdapterFactory
) : ListAdapter<BaseCatalogLibraryDM, CatalogLibraryAbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLibraryAbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return catalogHomepageAdapterFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: CatalogLibraryAbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<BaseCatalogLibraryDM>, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else {
            currentList[position]?.type(catalogHomepageAdapterFactory) ?: HideViewHolder.LAYOUT
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun bind(
        holder: AbstractViewHolder<BaseCatalogLibraryDM>,
        item: BaseCatalogLibraryDM
    ) {
        holder.bind(item)
    }

    override fun getItemCount() = currentList.size

    override fun onViewAttachedToWindow(holder: CatalogLibraryAbstractViewHolder<*>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: CatalogLibraryAbstractViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }
}
