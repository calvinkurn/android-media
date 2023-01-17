package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant

data class CatalogProductLoadMoreDataModel(
    val name: String = ""
): BaseCatalogLibraryDataModel {
    override fun type() = CatalogLibraryConstant.CATALOG_PRODUCT_LOAD

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
