package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse

data class CatalogLihatListItemDM(
    val name: String = "",
    val type: String = "",
    val catalogLibraryChildDataListItem: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList,
    val rootCategoryId: String,
    val rootCategoryName: String,
    val isGrid: Boolean,
    val isAsc: Boolean,
    val isActive : Boolean = false
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
