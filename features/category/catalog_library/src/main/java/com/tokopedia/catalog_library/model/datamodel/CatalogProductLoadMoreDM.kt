package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.util.CatalogLibraryConstant

data class CatalogProductLoadMoreDM(
    val name: String = ""
) : BaseCatalogLibraryDM {
    override fun type() = CatalogLibraryConstant.CATALOG_PRODUCT_LOAD

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDM): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDM): Bundle? {
        return null
    }
}
