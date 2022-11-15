package com.tokopedia.catalog_library.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.viewholder.containers.*

class CatalogHomepageAdapterFactoryImpl(private val catalogLibraryListener: CatalogLibraryListener): BaseAdapterTypeFactory(),
    CatalogHomepageAdapterFactory {
    override fun type(data: CatalogSpecialDataModel): Int {
        return CatalogSpecialContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogRelevantDataModel): Int {
        return CatalogRelevantContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogListDataModel): Int {
        return CatalogListContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogLihatDataModel): Int {
        return CatalogLihatContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogTopFiveDataModel): Int {
        return CatalogTopFiveContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogMostViralDataModel): Int {
        return CatalogMostViralContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogLandingListDataModel): Int {
        return CatalogLandingPageListContainerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CatalogSpecialContainerViewHolder.LAYOUT -> CatalogSpecialContainerViewHolder(view, catalogLibraryListener)
            CatalogRelevantContainerViewHolder.LAYOUT -> CatalogRelevantContainerViewHolder(view, catalogLibraryListener)
            CatalogListContainerViewHolder.LAYOUT -> CatalogListContainerViewHolder(view, catalogLibraryListener)
            CatalogLihatContainerViewHolder.LAYOUT -> CatalogLihatContainerViewHolder(view, catalogLibraryListener)
            CatalogTopFiveContainerViewHolder.LAYOUT -> CatalogTopFiveContainerViewHolder(view, catalogLibraryListener)
            CatalogMostViralContainerViewHolder.LAYOUT -> CatalogMostViralContainerViewHolder(view, catalogLibraryListener)
            CatalogLandingPageListContainerViewHolder.LAYOUT -> CatalogLandingPageListContainerViewHolder(view, catalogLibraryListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
