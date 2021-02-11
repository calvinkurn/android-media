package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel

class CatalogUiUpdater(var mapOfData: MutableMap<String, BaseCatalogDataModel>) {

    val productInfoMap: CatalogInfoDataModel?
        get() = mapOfData[CatalogConstant.CATALOG_INFO] as? CatalogInfoDataModel

    val specificationsMap: CatalogSpecificationDataModel?
        get() = mapOfData[CatalogConstant.CATALOG_SPECIFICATION] as? CatalogSpecificationDataModel

    fun updateModel(model: BaseCatalogDataModel){
        updateData(model.type(),model)
    }

    fun updateProductInfo(catalogInfoDataModel: BaseCatalogDataModel){
        updateData(CatalogConstant.CATALOG_INFO,catalogInfoDataModel)
    }

    fun updateSpecifications(specificationDataModel: BaseCatalogDataModel){
        updateData(CatalogConstant.CATALOG_SPECIFICATION,specificationDataModel)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseCatalogDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }
}