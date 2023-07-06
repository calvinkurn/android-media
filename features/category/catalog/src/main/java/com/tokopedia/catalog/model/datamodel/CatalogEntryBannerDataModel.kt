package com.tokopedia.catalog.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactory
import com.tokopedia.catalog.model.raw.CatalogImage

data class CatalogEntryBannerDataModel(
    val name: String = "",
    val type: String = "",
    val categoryName: String? = "",
    val categoryProductCount: String? = "",
    val catalogs: ArrayList<CatalogImage>?
) :
    BaseCatalogDataModel {

    override fun name(): String = name

    override fun type(): String = type

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
