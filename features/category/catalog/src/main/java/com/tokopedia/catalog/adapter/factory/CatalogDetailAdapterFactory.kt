package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.model.datamodel.CatalogTopSpecificationDataModel
import com.tokopedia.catalog.model.datamodel.CatalogVideoDataModel
import com.tokopedia.catalog.viewholder.CatalogProductsContainerViewHolder

interface CatalogDetailAdapterFactory {

    fun type(data : CatalogInfoDataModel) : Int
    fun type(data : CatalogTopSpecificationDataModel) : Int
    fun type(data : CatalogProductsContainerDataModel) : Int
    fun type(data : CatalogVideoDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}