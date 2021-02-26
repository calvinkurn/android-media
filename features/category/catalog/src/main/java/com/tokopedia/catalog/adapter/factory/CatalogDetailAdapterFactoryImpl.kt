package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.model.datamodel.CatalogTopSpecificationDataModel
import com.tokopedia.catalog.viewholder.CatalogInfoViewHolder
import com.tokopedia.catalog.viewholder.CatalogProductsContainerViewHolder
import com.tokopedia.catalog.viewholder.CatalogSpecificationsContainerViewHolder

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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type){
            CatalogInfoViewHolder.LAYOUT -> CatalogInfoViewHolder(view,catalogDetailListener)
            CatalogSpecificationsContainerViewHolder.LAYOUT -> CatalogSpecificationsContainerViewHolder(view,catalogDetailListener)
            CatalogProductsContainerViewHolder.LAYOUT -> CatalogProductsContainerViewHolder(view,catalogDetailListener)
            else -> super.createViewHolder(view,type)
        }
    }

}