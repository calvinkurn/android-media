package com.tokopedia.catalog.model.datamodel


data class CatalogDetailDataModel (
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()
)