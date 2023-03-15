package com.tokopedia.catalog_library.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.model.datamodel.*

interface CatalogHomepageAdapterFactory {

    fun type(data: CatalogSpecialDM): Int
    fun type(data: CatalogRelevantDM): Int
    fun type(data: CatalogPopularBrandsDM): Int
    fun type(data: CatalogPopularBrandsListDM): Int
    fun type(data: CatalogProductDM): Int

    fun type(data: CatalogLihatDM): Int
    fun type(data: CatalogLihatItemDM): Int

    fun type(data: CatalogTopFiveDM): Int
    fun type(data: CatalogMostViralDM): Int

    fun type(data: CatalogShimmerDM): Int
    fun type(data: CatalogContainerDM): Int
    fun type(data: CatalogProductLoadMoreDM): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
