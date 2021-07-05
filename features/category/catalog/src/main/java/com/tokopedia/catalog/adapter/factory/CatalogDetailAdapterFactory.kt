package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.model.datamodel.*

interface CatalogDetailAdapterFactory {

    fun type(data : CatalogInfoDataModel) : Int
    fun type(data : CatalogTopSpecificationDataModel) : Int
    fun type(data : CatalogProductsContainerDataModel) : Int
    fun type(data : CatalogVideoDataModel) : Int
    fun type(data : CatalogComparisionDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}