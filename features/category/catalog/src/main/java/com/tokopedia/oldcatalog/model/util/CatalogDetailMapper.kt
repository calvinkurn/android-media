package com.tokopedia.oldcatalog.model.util

import com.tokopedia.oldcatalog.model.datamodel.*
import com.tokopedia.oldcatalog.model.raw.*

object CatalogDetailMapper {

    fun mapIntoVisitable(comparedCatalogId: String, catalogGetDetailModular: CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents: MutableList<BaseCatalogDataModel> = mutableListOf()

        val comparisonInfoCatalogId = catalogGetDetailModular.comparisonInfoComponentData?.id ?: ""
        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(
                CatalogInfoDataModel(
                    name = CatalogConstant.CATALOG_INFO_NAME, type = CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, departmentId = departmentID, tag = tag,
                    priceRange = "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}",
                    description = description, shortDescription = shortDescription, comparisonInfoCatalogId = comparisonInfoCatalogId,
                    images = catalogGetDetailModular.basicInfo.catalogImage, url = url
                )
            )
        }

        var baseCatalogTopSpecs = arrayListOf<TopSpecificationsComponentData>()

        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when (component.type) {
                CatalogConstant.TOP_SPECIFICATIONS -> {
                    val crudeTopSpecificationsData = component.data
                    val topSpecsArray = arrayListOf<TopSpecificationsComponentData>()
                    crudeTopSpecificationsData?.forEachIndexed { _, componentData ->
                        topSpecsArray.add(
                            TopSpecificationsComponentData(
                                componentData.key,
                                componentData.value,
                                componentData.icon
                            )
                        )
                    }
                    baseCatalogTopSpecs = topSpecsArray
                    listOfComponents.add(CatalogTopSpecificationDataModel(name = component.name, type = component.type, topSpecificationsList = topSpecsArray))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {
                    listOfComponents.add(
                        CatalogProductsContainerDataModel(
                            name = component.name,
                            type = component.type,
                            catalogId = catalogGetDetailModular.basicInfo.id,
                            catalogName = catalogGetDetailModular.basicInfo.name ?: "",
                            catalogUrl = catalogGetDetailModular.basicInfo.url,
                            categoryId = catalogGetDetailModular.basicInfo.departmentID,
                            catalogBrand = catalogGetDetailModular.basicInfo.brand,
                            productSortingStatus = catalogGetDetailModular.basicInfo.productSortingStatus
                        )
                    )
                }

                CatalogConstant.VIDEO -> {
                    val crudeVideoData = component.data
                    val videoArray = arrayListOf<VideoComponentData>()
                    crudeVideoData?.forEachIndexed { _, componentData ->
                        videoArray.add(
                            VideoComponentData(
                                componentData.url,
                                componentData.type,
                                componentData.videoId,
                                componentData.thumbnail,
                                componentData.title,
                                componentData.author
                            )
                        )
                    }
                    listOfComponents.add(CatalogVideoDataModel(name = component.name, type = component.type, videosList = videoArray))
                }

                CatalogConstant.REVIEW -> {
                    val crudeReviewData = component.data
                    listOfComponents.addAll(
                        mapIntoReviewDataModel(
                            catalogGetDetailModular.basicInfo.name ?: "",
                            catalogGetDetailModular.basicInfo.id,
                            component.name,
                            component.type,
                            crudeReviewData
                        )
                    )
                }

                CatalogConstant.CATALOG_LIBRARY_ENRTY_POINT -> {
                    val crudeEntryPointData = component.data
                    listOfComponents.add(
                        mapIntoEntryPointData(
                            component.name,
                            component.type,
                            crudeEntryPointData
                        )
                    )
                }

                CatalogConstant.COMPARISON_NEW -> {
                    component.data?.firstOrNull()?.let { comparisonDataNew ->
                        listOfComponents.add(getNewComparisonComponent(catalogGetDetailModular, comparisonDataNew))
                    }
                }
            }
        }
        return listOfComponents
    }

    private fun mapIntoEntryPointData(name: String, type: String, crudeEntryPointData: List<ComponentData>?): BaseCatalogDataModel {
        val data = crudeEntryPointData?.firstOrNull()
        return CatalogEntryBannerDataModel(name, type, data?.categoryName, data?.catalogCount, data?.catalogs)
    }

    private fun getNewComparisonComponent(
        catalogGetDetailModular: CatalogResponseData.CatalogGetDetailModular,
        comparisonComponentDataNew: ComponentData
    ): CatalogComparisonNewDataModel {
        val specsListCombined = arrayListOf<ComponentData.SpecList>()
        specsListCombined.add(
            ComponentData.SpecList(
                "",
                arrayListOf(
                    ComponentData.SpecList.Subcard(
                        "",
                        "",
                        "",
                        ComparisonNewModel(
                            catalogGetDetailModular.basicInfo.id,
                            catalogGetDetailModular.basicInfo.brand,
                            catalogGetDetailModular.basicInfo.name,
                            "${catalogGetDetailModular.basicInfo.marketPrice?.firstOrNull()?.minFmt} - ${catalogGetDetailModular.basicInfo.marketPrice?.firstOrNull()?.maxFmt}",
                            catalogGetDetailModular.basicInfo.catalogImage?.firstOrNull()?.imageURL,""
                        ),
                        ComparisonNewModel(
                            comparisonComponentDataNew.comparedData?.id ?: "",
                            comparisonComponentDataNew.comparedData?.brand ?: "",
                            comparisonComponentDataNew.comparedData?.name ?: "",
                            "${comparisonComponentDataNew.comparedData?.marketPrice?.firstOrNull()?.minFmt} - ${comparisonComponentDataNew.comparedData?.marketPrice?.firstOrNull()?.maxFmt}",
                            comparisonComponentDataNew.comparedData?.catalogImage?.firstOrNull()?.imageURL ?: "",""
                        )
                    )
                ),
                false
            )
        )

        comparisonComponentDataNew.specList?.forEachIndexed { index, specList ->
            if (index == 0) {
                specList.isExpanded = true
            }
            specsListCombined.add(specList)
        }

        comparisonComponentDataNew.specList = specsListCombined
        return CatalogComparisonNewDataModel(CatalogConstant.COMPARISON_NEW, CatalogConstant.COMPARISON_NEW, comparisonComponentDataNew.specList)
    }

    private fun mapIntoReviewDataModel(
        catalogName: String,
        catalogId: String,
        componentName: String,
        componentType: String,
        crudeReviewData: List<ComponentData>?
    ): List<BaseCatalogDataModel> {
        val listOfReviewComponents = ArrayList<BaseCatalogDataModel>()
        crudeReviewData?.firstOrNull()?.let { componentData ->
            listOfReviewComponents.add(
                CatalogReviewDataModel(
                    componentName,
                    type = componentType,
                    data = ReviewComponentData(
                        catalogName,
                        catalogId,
                        componentData.avgRating,
                        componentData.reviews,
                        componentData.totalHelpfulReview
                    )
                )
            )
        }
        return listOfReviewComponents
    }

    fun getFullSpecificationsModel(catalogGetDetailModular: CatalogResponseData.CatalogGetDetailModular): CatalogFullSpecificationDataModel {
        var catalogFullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())
        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when (component.type) {
                CatalogConstant.FULL_CATALOG_SPECIFICATION -> {
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<FullSpecificationsComponentData>()
                    crudeSpecificationsData?.forEachIndexed { _, componentData ->
                        specifications.add(
                            FullSpecificationsComponentData(
                                componentData.name,
                                componentData.icon,
                                componentData.specificationsRow
                                    ?: arrayListOf()
                            )
                        )
                    }
                    catalogFullSpecificationDataModel = CatalogFullSpecificationDataModel(fullSpecificationsList = specifications)
                }
            }
        }
        return catalogFullSpecificationDataModel
    }
}
