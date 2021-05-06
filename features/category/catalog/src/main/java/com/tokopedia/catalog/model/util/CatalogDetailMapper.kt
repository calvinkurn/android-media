package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.*
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.model.raw.FullSpecificationsComponentData
import com.tokopedia.catalog.model.raw.TopSpecificationsComponentData

object CatalogDetailMapper {

    fun mapIntoVisitable(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = CatalogConstant.CATALOG_INFO_NAME, type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, tag = tag,
                    priceRange = "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}" ,
                    description = description, shortDescription = shortDescription,
                    images = catalogGetDetailModular.basicInfo.catalogImage, url = url))
        }

        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when(component.type){
                CatalogConstant.TOP_SPECIFICATIONS -> {
                    val crudeTopSpecificationsData = component.data
                    val topSpecsArray = arrayListOf<TopSpecificationsComponentData>()
                    crudeTopSpecificationsData?.forEachIndexed { _, componentData ->
                        topSpecsArray.add(TopSpecificationsComponentData(componentData.key,
                                componentData.value, componentData.icon))
                    }
                    listOfComponents.add(CatalogTopSpecificationDataModel(name = component.name, type = component.type , topSpecificationsList = topSpecsArray))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {
                    listOfComponents.add(CatalogProductsContainerDataModel(name = component.name, type = component.type,
                            catalogId = catalogGetDetailModular.basicInfo.id, catalogUrl = catalogGetDetailModular.basicInfo.url))
                }
            }
        }

        return listOfComponents
    }

    fun getFullSpecificationsModel(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular) : CatalogFullSpecificationDataModel{
        var catalogFullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())
        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when (component.type) {
                CatalogConstant.FULL_CATALOG_SPECIFICATION -> {
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<FullSpecificationsComponentData>()
                    crudeSpecificationsData?.forEachIndexed { _, componentData ->
                        specifications.add(FullSpecificationsComponentData(componentData.name,
                                componentData.icon, componentData.specificationsRow
                                ?: arrayListOf()))
                    }
                    catalogFullSpecificationDataModel =  CatalogFullSpecificationDataModel(fullSpecificationsList = specifications)
                }
            }
        }
        return catalogFullSpecificationDataModel
    }
}