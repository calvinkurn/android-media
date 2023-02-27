package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.kotlin.model.ImpressHolder

data class CatalogProductDataModel(
    val name: String = "",
    val type: String = "",
    val catalogProduct: CatalogListResponse.CatalogGetList.CatalogsProduct
) : BaseCatalogLibraryDataModel {
    val impressHolder: ImpressHolder = ImpressHolder()

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
