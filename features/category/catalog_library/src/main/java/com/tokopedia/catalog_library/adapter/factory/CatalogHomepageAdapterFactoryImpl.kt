package com.tokopedia.catalog_library.adapter.factory

import android.view.View
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.viewholder.*

class CatalogHomepageAdapterFactoryImpl(private val catalogLibraryListener: CatalogLibraryListener) :
    BaseAdapterTypeFactory(),
    CatalogHomepageAdapterFactory {

    private val recycledViewPool = RecycledViewPool()

    override fun type(data: CatalogSpecialDM): Int {
        return CatalogSpecialItemVH.LAYOUT
    }

    override fun type(data: CatalogRelevantDM): Int {
        return CatalogRelevantItemVH.LAYOUT
    }

    override fun type(data: CatalogProductDM): Int {
        return CatalogProductItemVH.LAYOUT
    }

    override fun type(data: CatalogPopularBrandsDM): Int {
        return CatalogPopularBrandsItemVH.LAYOUT
    }

    override fun type(data: CatalogPopularBrandsListDM): Int {
        return CatalogPopularBrandsWithCatalogsItemVH.LAYOUT
    }

    override fun type(data: CatalogLihatDM): Int {
        return CatalogLihatVH.LAYOUT
    }

    override fun type(data: CatalogLihatItemDM): Int {
        return CatalogLihatItemVH.LAYOUT
    }

    override fun type(data: CatalogTopFiveDM): Int {
        return CatalogTopFiveItemVH.LAYOUT
    }

    override fun type(data: CatalogMostViralDM): Int {
        return CatalogMostViralItemVH.LAYOUT
    }

    override fun type(data: CatalogShimmerDM): Int {
        return CatalogShimmerVH.LAYOUT
    }

    override fun type(data: CatalogContainerDM): Int {
        return CatalogContainerItemVH.LAYOUT
    }

    override fun type(data: CatalogProductLoadMoreDM): Int {
        return CatalogProductLoadMoreVH.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CatalogTopFiveItemVH.LAYOUT -> CatalogTopFiveItemVH(
                view,
                catalogLibraryListener
            )
            CatalogMostViralItemVH.LAYOUT -> CatalogMostViralItemVH(
                view,
                catalogLibraryListener
            )
            CatalogSpecialItemVH.LAYOUT -> CatalogSpecialItemVH(
                view,
                catalogLibraryListener
            )
            CatalogRelevantItemVH.LAYOUT -> CatalogRelevantItemVH(
                view,
                catalogLibraryListener
            )
            CatalogPopularBrandsItemVH.LAYOUT -> CatalogPopularBrandsItemVH(
                view,
                catalogLibraryListener
            )
            CatalogPopularBrandsWithCatalogsItemVH.LAYOUT -> CatalogPopularBrandsWithCatalogsItemVH(
                view,
                catalogLibraryListener
            )
            CatalogLihatVH.LAYOUT -> CatalogLihatVH(view, catalogLibraryListener, recycledViewPool)
            CatalogLihatItemVH.LAYOUT -> CatalogLihatItemVH(
                view,
                catalogLibraryListener
            )
            CatalogProductItemVH.LAYOUT -> CatalogProductItemVH(
                view,
                catalogLibraryListener
            )
            CatalogContainerItemVH.LAYOUT -> CatalogContainerItemVH(
                view,
                catalogLibraryListener
            )
            CatalogProductLoadMoreVH.LAYOUT -> CatalogProductLoadMoreVH(
                view
            )
            CatalogShimmerVH.LAYOUT -> CatalogShimmerVH(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
