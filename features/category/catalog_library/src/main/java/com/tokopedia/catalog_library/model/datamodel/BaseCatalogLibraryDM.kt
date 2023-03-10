package com.tokopedia.catalog_library.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactory

interface BaseCatalogLibraryDM : Visitable<CatalogHomepageAdapterFactory> {
    fun type(): String
    fun name(): String
    fun equalsWith(newData: BaseCatalogLibraryDM): Boolean
    fun getChangePayload(newData: BaseCatalogLibraryDM) : Bundle?
}
