package com.tokopedia.catalog.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory

data class CatalogImageDataModel(val name : String = "" , val type : String = "",
                                 val imageList : ArrayList<CatalogImageItem>) : BaseCatalogDataModel {

    override fun name(): String = name

    override fun type(): String  = type

    override fun type(typeFactory: CatalogDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseCatalogDataModel): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogDataModel): Bundle? {
        return null
    }

}

data class CatalogImageItem(val name : String? , val url : String?)