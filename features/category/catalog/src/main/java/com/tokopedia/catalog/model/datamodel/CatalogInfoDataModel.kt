package com.tokopedia.catalog.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.catalog.model.raw.CatalogImage

data class CatalogInfoDataModel (val name : String = "" , val type : String = "",
                                 val productName : String?, val productBrand : String?, val tag : String?,
                                 val priceRange : String? , val description : String?, val shortDescription : String?,
                                 val images : ArrayList<CatalogImage>?
                                ) : BaseCatalogDataModel {

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



