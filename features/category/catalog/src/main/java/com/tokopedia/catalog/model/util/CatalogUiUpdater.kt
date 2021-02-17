package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogFullSpecificationDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogTopSpecificationDataModel

class CatalogUiUpdater(var mapOfData: MutableMap<String, BaseCatalogDataModel>) {

    val productInfoMap: CatalogInfoDataModel?
        get() = mapOfData[CatalogConstant.CATALOG_INFO] as? CatalogInfoDataModel

    val topSpecificationsMap: CatalogTopSpecificationDataModel?
        get() = mapOfData[CatalogConstant.TOP_SPECIFICATIONS] as? CatalogTopSpecificationDataModel

    fun updateModel(model: BaseCatalogDataModel){
        updateData(model.type(),model)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseCatalogDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }
}