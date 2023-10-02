package com.tokopedia.oldcatalog.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldcatalog.adapter.factory.CatalogDetailAdapterFactory

interface BaseCatalogDataModel : Visitable<CatalogDetailAdapterFactory> {
    fun type(): String
    fun name(): String
    fun equalsWith(newData: BaseCatalogDataModel): Boolean
    fun getChangePayload(newData: BaseCatalogDataModel) : Bundle?
}
