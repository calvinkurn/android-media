package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse

data class CatalogBrandCategoryDM(
    val name: String = "",
    val type: String = "",
    val catalogLibraryDataList: ArrayList<CatalogLibraryResponse.CategoryListLibraryPage.CategoryData>?,
    var selectedCategoryId: String = ""
) : BaseCatalogLibraryDM {

    override fun type() = type

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDM): Boolean {
        return newData == this
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDM): Bundle? {
        return null
    }
}
