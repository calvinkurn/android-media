package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogListResponse

data class CatalogMostViralDataModel(
    val name: String = "",
    val type: String = "",
    val catalogMostViralData: CatalogListResponse.CatalogGetList.CatalogsList
) : BaseCatalogLibraryDataModel {
    override fun type() = type

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDataModel): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDataModel): Bundle? {
        return null
    }

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
