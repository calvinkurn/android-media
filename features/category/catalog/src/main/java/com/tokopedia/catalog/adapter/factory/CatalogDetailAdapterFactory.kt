package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.model.datamodel.CatalogImageDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel

interface CatalogDetailAdapterFactory {

    fun type(data : CatalogInfoDataModel) : Int
    fun type(data : CatalogSpecificationDataModel) : Int
    fun type(data : CatalogImageDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}