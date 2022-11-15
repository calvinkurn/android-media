package com.tokopedia.catalog_library.model.datamodel

data class CatalogLibraryDataModel(
    val listOfComponents: MutableList<BaseCatalogLibraryDataModel> = mutableListOf()
)
