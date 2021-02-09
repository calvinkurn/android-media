package com.tokopedia.catalog.model.datamodel

import com.tokopedia.catalog.model.raw.CatalogResponse

data class CatalogDetailDataModel (
        val basicInfo: CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo,
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()
)