package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse

data class CatalogSpecialDataModel(
    val name: String = "",
    val type: String = "",
    val specialDataList: ArrayList<CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData>?
): BaseCatalogLibraryDataModel {

    override fun type() = type

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDataModel): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDataModel): Bundle? {
        return null
    }
}
