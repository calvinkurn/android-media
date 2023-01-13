package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse

data class CatalogLihatItemDataModel(
    val name: String = "",
    val type: String = "",
    val catalogLibraryChildDataListItem: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList
) : BaseCatalogLibraryDataModel {

    override fun type() = type

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDataModel): Boolean {
        return newData == this
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDataModel): Bundle? {
        return null
    }
}
