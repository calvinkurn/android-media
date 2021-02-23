package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogTopSpecificationDataModel

interface CatalogDetailAdapterFactory {

    fun type(data : CatalogInfoDataModel) : Int
    fun type(data : CatalogTopSpecificationDataModel) : Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}