package com.tokopedia.oldcatalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.*
import com.tokopedia.oldcatalog.viewholder.components.CatalogForYouViewHolder
import com.tokopedia.oldcatalog.viewholder.components.CatalogInfoViewHolder
import com.tokopedia.oldcatalog.viewholder.components.CatalogStaggeredProductCardItemVH
import com.tokopedia.oldcatalog.viewholder.components.CatalogEntryBannerViewHolder
import com.tokopedia.oldcatalog.viewholder.containers.*
import com.tokopedia.oldcatalog.viewholder.shimmer.CatalogForYouShimmerCardItemVH
import com.tokopedia.oldcatalog.viewholder.shimmer.CatalogStaggeredShimmerCardItemVH

class CatalogDetailAdapterFactoryImpl(private val catalogDetailListener: CatalogDetailListener) : BaseAdapterTypeFactory(), CatalogDetailAdapterFactory {

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

    override fun type(data: CatalogReviewDataModel): Int {
        return CatalogReviewContainerViewHolder.LAYOUT
    }

    override fun type(data: CatalogStaggeredShimmerModel): Int {
        return CatalogStaggeredShimmerCardItemVH.LAYOUT
    }

    override fun type(data: CatalogStaggeredProductModel): Int {
        return CatalogStaggeredProductCardItemVH.LAYOUT
    }

    override fun type(data: CatalogForYouShimmerModel): Int {
        return CatalogForYouShimmerCardItemVH.LAYOUT
    }

    override fun type(data: CatalogForYouModel): Int {
        return CatalogForYouViewHolder.LAYOUT
    }

    override fun type(data: CatalogComparisonNewDataModel): Int {
        return CatalogComparisonContainerNewViewHolder.LAYOUT
    }

    override fun type(data: CatalogEntryBannerDataModel): Int {
        return CatalogEntryBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CatalogEntryBannerViewHolder.LAYOUT -> CatalogEntryBannerViewHolder(view, catalogDetailListener)
            CatalogInfoViewHolder.LAYOUT -> CatalogInfoViewHolder(view, catalogDetailListener)
            CatalogSpecificationsContainerViewHolder.LAYOUT -> CatalogSpecificationsContainerViewHolder(view, catalogDetailListener)
            CatalogVideosContainerViewHolder.LAYOUT -> CatalogVideosContainerViewHolder(view, catalogDetailListener)
            CatalogReviewContainerViewHolder.LAYOUT -> CatalogReviewContainerViewHolder(view, catalogDetailListener)
            CatalogProductsContainerViewHolder.LAYOUT -> CatalogProductsContainerViewHolder(view, catalogDetailListener)
            CatalogStaggeredProductCardItemVH.LAYOUT -> CatalogStaggeredProductCardItemVH(view, catalogDetailListener)
            CatalogStaggeredShimmerCardItemVH.LAYOUT -> CatalogStaggeredShimmerCardItemVH(view)
            CatalogForYouShimmerCardItemVH.LAYOUT -> CatalogForYouShimmerCardItemVH(view)
            CatalogForYouViewHolder.LAYOUT -> CatalogForYouViewHolder(view, catalogDetailListener)
            CatalogComparisonContainerNewViewHolder.LAYOUT -> CatalogComparisonContainerNewViewHolder(view, catalogDetailListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
