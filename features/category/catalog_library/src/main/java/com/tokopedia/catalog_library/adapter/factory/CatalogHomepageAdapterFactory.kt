package com.tokopedia.catalog_library.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.model.datamodel.*

interface CatalogHomepageAdapterFactory {

    fun type(data: CatalogSpecialDataModel): Int
    fun type(data: CatalogRelevantDataModel): Int
    fun type(data: CatalogProductDataModel): Int

    fun type(data: CatalogLihatDataModel): Int
    fun type(data: CatalogLihatItemDataModel): Int

    fun type(data: CatalogTopFiveDataModel): Int
    fun type(data: CatalogMostViralDataModel): Int

    fun type(data: CatalogShimmerDataModel): Int
    fun type(data: CatalogContainerDataModel): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
