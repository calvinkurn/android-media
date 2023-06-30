package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse

data class CatalogPopularBrandsListDM(
    val name: String = "",
    val type: String = "",
    val brandsList: CatalogBrandsPopularResponse.CatalogGetBrandPopular.Brands
) : BaseCatalogLibraryDM {

    override fun type() = type

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
