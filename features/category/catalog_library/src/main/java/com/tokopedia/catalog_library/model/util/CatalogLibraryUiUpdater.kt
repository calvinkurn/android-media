package com.tokopedia.catalog_library.model.util

import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel

class CatalogLibraryUiUpdater(var mapOfData: MutableMap<String, BaseCatalogLibraryDataModel>) {

    fun updateModel(model: BaseCatalogLibraryDataModel){
        updateData(model.type(),model)
    }

    private fun updateData(key: String, baseCatalogHomeDataModel: BaseCatalogLibraryDataModel) {
        mapOfData[key] = baseCatalogHomeDataModel
    }
}
