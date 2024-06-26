package com.tokopedia.oldcatalog.model.datamodel

import android.os.Bundle
import com.tokopedia.oldcatalog.adapter.factory.CatalogDetailAdapterFactory

data class CatalogProductsContainerDataModel (val name : String = "", val type : String = "",
                                              val catalogId: String,
                                              val catalogName : String,
                                              val catalogUrl : String?,
                                              val categoryId : String?,
                                              val catalogBrand : String?,
                                              val productSortingStatus : Int?)
    : BaseCatalogDataModel {

    override fun name(): String = name

    override fun type(): String  = type

    override fun type(typeFactory: CatalogDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseCatalogDataModel): Boolean {
        return true
    }

    override fun getChangePayload(newData: BaseCatalogDataModel): Bundle? {
        return null
    }

}

