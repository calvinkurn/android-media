package com.tokopedia.oldcatalog.model.datamodel


data class CatalogDetailDataModel (
        val fullSpecificationDataModel: CatalogFullSpecificationDataModel,
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()
)
