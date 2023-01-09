package com.tokopedia.catalog_library.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.viewholder.components.*
import com.tokopedia.catalog_library.viewholder.containers.CatalogLandingPageListContainerViewHolder

class CatalogHomepageAdapterFactoryImpl(private val catalogLibraryListener: CatalogLibraryListener): BaseAdapterTypeFactory(),
    CatalogHomepageAdapterFactory {
    override fun type(data: CatalogSpecialDataModel): Int {
        return CatalogSpecialItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogRelevantDataModel): Int {
        return CatalogRelevantItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogProductDataModel): Int {
        return CatalogProductItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogLihatDataModel): Int {
        return CatalogLihatViewHolder.LAYOUT
    }

    override fun type(data: CatalogLihatItemDataModel): Int {
        return CatalogLihatItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogTopFiveDataModel): Int {
        return CatalogTopFiveItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogMostViralDataModel): Int {
        return CatalogMostViralItemViewHolder.LAYOUT
    }

    override fun type(data: CatalogLandingListDataModel): Int {
        return CatalogLandingPageListContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogShimmerDataModel): Int {
        return CatalogShimmerViewHolder.LAYOUT
    }

    override fun type(data: CatalogContainerDataModel): Int {
        return CatalogContainerItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CatalogTopFiveItemViewHolder.LAYOUT -> CatalogTopFiveItemViewHolder(view,catalogLibraryListener)
            CatalogMostViralItemViewHolder.LAYOUT -> CatalogMostViralItemViewHolder(view,catalogLibraryListener)
            CatalogSpecialItemViewHolder.LAYOUT -> CatalogSpecialItemViewHolder(view,catalogLibraryListener)
            CatalogRelevantItemViewHolder.LAYOUT -> CatalogRelevantItemViewHolder(view, catalogLibraryListener)
            CatalogLihatViewHolder.LAYOUT -> CatalogLihatViewHolder(view, catalogLibraryListener)
            CatalogLihatItemViewHolder.LAYOUT -> CatalogLihatItemViewHolder(view, catalogLibraryListener)
            CatalogLandingPageListContainerViewHolder.LAYOUT -> CatalogLandingPageListContainerViewHolder(
                view,
                catalogLibraryListener
            )
            CatalogProductItemViewHolder.LAYOUT -> CatalogProductItemViewHolder(
                view,
                catalogLibraryListener
            )
            CatalogContainerItemViewHolder.LAYOUT -> CatalogContainerItemViewHolder(
                view,
                catalogLibraryListener
            )
            CatalogShimmerViewHolder.LAYOUT -> CatalogShimmerViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
