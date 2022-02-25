package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.*
import com.tokopedia.catalog.viewholder.components.CatalogInfoViewHolder
import com.tokopedia.catalog.viewholder.components.CatalogStaggeredProductCardItemVH
import com.tokopedia.catalog.viewholder.containers.*
import com.tokopedia.catalog.viewholder.shimmer.CatalogStaggeredShimmerCardItemVH

class CatalogDetailAdapterFactoryImpl(private val catalogDetailListener: CatalogDetailListener) : BaseAdapterTypeFactory() , CatalogDetailAdapterFactory {

    override fun type(data: CatalogInfoDataModel): Int {
        return CatalogInfoViewHolder.LAYOUT
    }

    override fun type(data: CatalogTopSpecificationDataModel): Int {
        return CatalogSpecificationsContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogProductsContainerDataModel): Int {
        return CatalogProductsContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogVideoDataModel): Int {
        return CatalogVideosContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogComparisionDataModel): Int {
        return CatalogComparisionContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogReviewDataModel): Int {
        return CatalogReviewContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogStaggeredShimmerModel): Int {
        return CatalogStaggeredShimmerCardItemVH.LAYOUT
    }

    override fun type(data: CatalogStaggeredProductModel): Int {
        return CatalogStaggeredProductCardItemVH.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type){
            CatalogInfoViewHolder.LAYOUT -> CatalogInfoViewHolder(view, catalogDetailListener)
            CatalogSpecificationsContainerViewHolder.LAYOUT -> CatalogSpecificationsContainerViewHolder(view, catalogDetailListener)
            CatalogVideosContainerViewHolder.LAYOUT -> CatalogVideosContainerViewHolder(view, catalogDetailListener)
            CatalogComparisionContainerViewHolder.LAYOUT -> CatalogComparisionContainerViewHolder(view, catalogDetailListener)
            CatalogReviewContainerViewHolder.LAYOUT -> CatalogReviewContainerViewHolder(view, catalogDetailListener)
            CatalogProductsContainerViewHolder.LAYOUT -> CatalogProductsContainerViewHolder(view, catalogDetailListener)
            CatalogStaggeredProductCardItemVH.LAYOUT -> CatalogStaggeredProductCardItemVH(view,catalogDetailListener)
            CatalogStaggeredShimmerCardItemVH.LAYOUT -> CatalogStaggeredShimmerCardItemVH(view)
            else -> super.createViewHolder(view,type)
        }
    }

}