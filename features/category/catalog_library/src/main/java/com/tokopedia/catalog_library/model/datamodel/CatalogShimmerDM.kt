package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory

data class CatalogShimmerDM(
    val name: String = "",
    val type: String = "",
    val shimmerType: Int
) : BaseCatalogLibraryDM {
    override fun type() = type

    override fun name() = name

    override fun equalsWith(newData: BaseCatalogLibraryDM): Boolean {
        return false
    }

    override fun getChangePayload(newData: BaseCatalogLibraryDM): Bundle? {
        return null
    }

    override fun type(typeFactory: CatalogHomepageAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
