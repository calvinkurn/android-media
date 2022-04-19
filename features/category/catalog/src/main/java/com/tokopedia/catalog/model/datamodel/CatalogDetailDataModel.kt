package com.tokopedia.catalog.model.datamodel


data class CatalogDetailDataModel (
        val fullSpecificationDataModel: CatalogFullSpecificationDataModel,
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()
)