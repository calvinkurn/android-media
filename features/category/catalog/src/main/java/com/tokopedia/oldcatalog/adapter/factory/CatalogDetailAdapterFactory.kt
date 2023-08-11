package com.tokopedia.oldcatalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.oldcatalog.model.datamodel.*

interface CatalogDetailAdapterFactory {

    fun type(data: CatalogInfoDataModel): Int
    fun type(data: CatalogTopSpecificationDataModel): Int
    fun type(data: CatalogProductsContainerDataModel): Int
    fun type(data: CatalogVideoDataModel): Int
    fun type(data: CatalogReviewDataModel): Int
    fun type(data: CatalogStaggeredShimmerModel): Int
    fun type(data: CatalogStaggeredProductModel): Int
    fun type(data: CatalogForYouShimmerModel): Int
    fun type(data: CatalogForYouModel): Int
    fun type(data: CatalogComparisonNewDataModel): Int
    fun type(data: CatalogEntryBannerDataModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
