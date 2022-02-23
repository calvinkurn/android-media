package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.*
import com.tokopedia.catalog.model.raw.*

object CatalogDetailMapper {

    fun mapIntoVisitable(comparedCatalogId : String, catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        val comparisonInfoCatalogId = catalogGetDetailModular.comparisonInfoComponentData?.id ?: ""
        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = CatalogConstant.CATALOG_INFO_NAME, type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, departmentId = departmentID, tag = tag,
                    priceRange = "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}" ,
                    description = description, shortDescription = shortDescription, comparisonInfoCatalogId = comparisonInfoCatalogId,
                    images = catalogGetDetailModular.basicInfo.catalogImage, url = url))
        }

        var baseCatalogTopSpecs = arrayListOf<TopSpecificationsComponentData>()

        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when(component.type){
                CatalogConstant.TOP_SPECIFICATIONS -> {
                    val crudeTopSpecificationsData = component.data
                    val topSpecsArray = arrayListOf<TopSpecificationsComponentData>()
                    crudeTopSpecificationsData?.forEachIndexed { _, componentData ->
                        topSpecsArray.add(TopSpecificationsComponentData(componentData.key,
                                componentData.value, componentData.icon))
                    }
                    baseCatalogTopSpecs = topSpecsArray
                    listOfComponents.add(CatalogTopSpecificationDataModel(name = component.name, type = component.type , topSpecificationsList = topSpecsArray))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {
                    listOfComponents.add(CatalogProductsContainerDataModel(name = component.name, type = component.type,
                            catalogId = catalogGetDetailModular.basicInfo.id, catalogUrl = catalogGetDetailModular.basicInfo.url,
                            categoryId= catalogGetDetailModular.basicInfo.departmentID, catalogBrand = catalogGetDetailModular.basicInfo.brand
                        ))
                }

                CatalogConstant.VIDEO -> {
                    val crudeVideoData = component.data
                    val videoArray = arrayListOf<VideoComponentData>()
                    crudeVideoData?.forEachIndexed { _, componentData ->
                        videoArray.add(VideoComponentData(componentData.url,
                                componentData.type,componentData.videoId,componentData.thumbnail,componentData.title,componentData.author))
                    }
                    listOfComponents.add(CatalogVideoDataModel(name = component.name, type = component.type , videosList = videoArray ))
                }

                CatalogConstant.REVIEW -> {
                    val crudeReviewData = component.data
                    listOfComponents.addAll(mapIntoReviewDataModel(catalogGetDetailModular.basicInfo.name ?: "",
                        catalogGetDetailModular.basicInfo.id
                            , component.name, component.type, crudeReviewData))
                }

                CatalogConstant.COMPARISON -> {
                    component.data?.firstOrNull()?.let { comparisonData ->
                        if(comparedCatalogId.isBlank()) {
                            listOfComponents.add(getComparisonComponent(comparedCatalogId,catalogGetDetailModular,
                                baseCatalogTopSpecs,
                                comparisonData))
                        }
                    }
                }
            }
        }

        if(comparedCatalogId.isNotBlank()){
            catalogGetDetailModular.comparisonInfoComponentData?.run {
                listOfComponents.add(getComparisonComponent(comparedCatalogId,catalogGetDetailModular,
                    baseCatalogTopSpecs,
                    this))
            }
        }
        return listOfComponents
    }

    private fun getComparisonComponent(comparedCatalogId : String?, catalogGetDetailModular: CatalogResponseData.CatalogGetDetailModular,
                                       baseCatalogTopSpecs : ArrayList<TopSpecificationsComponentData>,
                                       comparisonComponentData: ComponentData) : CatalogComparisionDataModel {
        val keySet = LinkedHashSet<String>()
        val baseCatalog = HashMap<String, ComparisionModel>()
        val comparisonCatalog = HashMap<String,ComparisionModel>()

        keySet.add(CatalogConstant.COMPARISION_DETAIL)

        catalogGetDetailModular.basicInfo.run {
            baseCatalog.put(CatalogConstant.COMPARISION_DETAIL, ComparisionModel(id,brand,name,
                "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}",
                catalogImage?.firstOrNull()?.imageURL,null,null))
        }

        baseCatalogTopSpecs.forEach { topSpec ->
            if(!topSpec.key.isNullOrEmpty()){
                keySet.add(topSpec.key)
                baseCatalog[topSpec.key] = ComparisionModel(null,null,null,null,null,
                    topSpec.key,topSpec.value)
            }
        }

        if(comparedCatalogId.isNullOrBlank()){
            comparisonComponentData.topSpecifications?.forEach { comparisonTopSpec ->
                if(keySet.contains(comparisonTopSpec.key)){
                    comparisonCatalog[comparisonTopSpec.key] = ComparisionModel(null,null,null,null,null,
                        comparisonTopSpec.key,comparisonTopSpec.value)
                }
            }
        }else {
            comparisonComponentData.topSpecifications?.forEach { comparisonTopSpec ->
                if(keySet.contains(comparisonTopSpec.key)){
                    comparisonCatalog[comparisonTopSpec.key] = ComparisionModel(null,null,null,null,null,
                        comparisonTopSpec.key,comparisonTopSpec.value)
                }
            }
        }

        comparisonComponentData.run {
            comparisonCatalog.put(CatalogConstant.COMPARISION_DETAIL, ComparisionModel(id,brand,name,
                "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}",
                catalogImage?.firstOrNull()?.imageURL,null,null))
        }

        return CatalogComparisionDataModel(CatalogConstant.COMPARISON,CatalogConstant.COMPARISON,
            keySet,baseCatalog,comparisonCatalog)
    }

    private fun mapIntoReviewDataModel(catalogName : String, catalogId : String, componentName : String,
                                       componentType : String, crudeReviewData: List<ComponentData>?)
    : List<BaseCatalogDataModel> {
        val listOfReviewComponents = ArrayList<BaseCatalogDataModel>();
        crudeReviewData?.firstOrNull()?.let {  componentData ->
            listOfReviewComponents.add(CatalogReviewDataModel(componentName, type = componentType,
                    data = ReviewComponentData(catalogName,catalogId,componentData.avgRating,componentData.reviews,
                            componentData.totalHelpfulReview)))
        }
        return listOfReviewComponents
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