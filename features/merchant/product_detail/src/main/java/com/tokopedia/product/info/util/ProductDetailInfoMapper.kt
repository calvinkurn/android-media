package com.tokopedia.product.info.util

import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.data.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.view.models.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel.Companion.SPECIFICATION_SIZE_THRESHOLD
import com.tokopedia.product.info.view.models.ProductDetailInfoVisitable
import com.tokopedia.product.info.util.ProductDetailInfoConstant.CATALOG
import com.tokopedia.product.info.util.ProductDetailInfoConstant.CUSTOM_INFO_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.GUIDELINE_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.HEADER_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.SHOP_NOTES_DETAIL_KEY

/**
 * Created by Yehezkiel on 20/10/20
 */
object ProductDetailInfoMapper {

    fun generateVisitable(
        responseData: PdpGetDetailBottomSheet,
        parcelData: ProductInfoParcelData
    ): List<ProductDetailInfoVisitable> {
        val components = if (parcelData.isOpenSpecification) {
            generateVisitableSpecification(
                responseData = responseData,
                parcelData = parcelData
            )
        } else {
            generateVisitableDescription(
                responseData = responseData,
                parcelData = parcelData
            )
        }

        if (!parcelData.isTokoNow) {
            components.add(
                ProductDetailInfoDiscussionDataModel(
                    componentName = components.size + 1,
                    title = responseData.discussion.title,
                    discussionCount = parcelData.discussionCount,
                    isShowable = false
                )
            )
        }

        return components
    }

    private fun generateVisitableDescription(
        responseData: PdpGetDetailBottomSheet,
        parcelData: ProductInfoParcelData
    ): MutableList<ProductDetailInfoVisitable> {
        val listOfComponent: MutableList<ProductDetailInfoVisitable> = mutableListOf()
        val dataContent = parcelData.productInfo.dataContent

        responseData.bottomsheetData.forEachIndexed { index, it ->
            when (it.componentName) {
                HEADER_DETAIL_KEY -> listOfComponent.add(
                    generateHeaderDetailDescription(
                        componentId = index,
                        parcelData = parcelData
                    )
                )
                DESCRIPTION_DETAIL_KEY -> {
                    val descriptionValue = dataContent.firstOrNull {
                        it.title.lowercase() == DESCRIPTION_DETAIL_KEY
                    }?.subtitle

                    if (descriptionValue?.isNotEmpty() == true) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableDataModel(
                                componentName = index,
                                title = it.title,
                                textValue = descriptionValue,
                                youtubeVideo = parcelData.listOfYoutubeVideo,
                                isShowable = it.isShowable
                            )
                        )
                    }
                }
                GUIDELINE_DETAIL_KEY -> {
                    val variantGuideline = parcelData.variantGuideline
                    if (variantGuideline.isNotEmpty()) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableImageDataModel(
                                componentName = index,
                                title = it.title,
                                imageUrl = variantGuideline,
                                isShowable = it.isShowable
                            )
                        )
                    }
                }
                SHOP_NOTES_DETAIL_KEY -> {
                    val shopNotes = responseData.dataShopNotes
                    if (shopNotes.error.isEmpty() && shopNotes.shopNotesData.isNotEmpty()) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableListDataModel(
                                componentName = index,
                                title = it.title,
                                shopNotes = responseData.dataShopNotes.shopNotesData,
                                isShowable = it.isShowable
                            )
                        )
                    }
                }
                CUSTOM_INFO_KEY -> {
                    listOfComponent.add(
                        ProductDetailInfoCardDataModel(
                            componentName = index,
                            title = it.title,
                            image = it.icon,
                            applink = it.applink
                        )
                    )
                }
                CATALOG -> {
                    /**
                     * ignore this block, bcz render at [generateVisitableSpecification]
                     */
                }
                else -> {
                    if (it.value.isNotEmpty()) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableDataModel(
                                componentName = index,
                                title = it.title,
                                textValue = it.value,
                                youtubeVideo = listOf(),
                                isShowable = it.isShowable
                            )
                        )
                    }
                }
            }
        }

        return listOfComponent
    }

    private fun generateHeaderDetailDescription(
        componentId: Int,
        parcelData: ProductInfoParcelData
    ): ProductDetailInfoHeaderDataModel {
        val isCatalog = parcelData.isOpenCatalogDescription
        val productInfoItems = mutableListOf<ProductDetailInfoContent>()
        val annotationItems = mutableListOf<ProductDetailInfoContent>()
        val dataContent = parcelData.productInfo.dataContent

        dataContent.forEach { data ->
            if (data.title.lowercase() != DESCRIPTION_DETAIL_KEY) {
                val notShownMax = isInfoUnderMax(currentSize = productInfoItems.size)

                if (data.showAtBottomSheet && !isCatalog && notShownMax) {
                    productInfoItems.add(data)
                }

                if (data.isAnnotation) {
                    annotationItems.add(data)
                }
            }
        }

        return ProductDetailInfoHeaderDataModel(
            componentId = componentId,
            img = parcelData.productImageUrl,
            productTitle = parcelData.productTitle,
            listOfInfo = productInfoItems,
            listOfAnnotation = annotationItems
        )
    }

    private fun generateVisitableSpecification(
        responseData: PdpGetDetailBottomSheet,
        parcelData: ProductInfoParcelData
    ): MutableList<ProductDetailInfoVisitable> {
        val listOfComponent: MutableList<ProductDetailInfoVisitable> = mutableListOf()

        responseData.bottomsheetData.forEachIndexed { index, it ->
            when (it.componentName) {
                HEADER_DETAIL_KEY -> listOfComponent.add(
                    generateHeaderDetailSpecification(
                        componentId = index,
                        parcelData = parcelData
                    )
                )
                CATALOG -> {
                    listOfComponent.add(
                        ProductDetailInfoCatalogDataModel(
                            componentName = index,
                            title = it.title,
                            items = it.row
                        )
                    )
                }
            }
        }

        return listOfComponent
    }


    private fun generateHeaderDetailSpecification(
        componentId: Int,
        parcelData: ProductInfoParcelData
    ): ProductDetailInfoHeaderDataModel {
        val productInfoItems = mutableListOf<ProductDetailInfoContent>()
        val annotationItems = mutableListOf<ProductDetailInfoContent>()
        val dataContent = parcelData.productInfo.dataContent

        dataContent.forEach { data ->
            if (data.title.lowercase() != DESCRIPTION_DETAIL_KEY) {
                val notShownMax = isInfoUnderMax(currentSize = productInfoItems.size)

                if (data.showAtBottomSheet && notShownMax) {
                    productInfoItems.add(data)
                }

                if (data.isAnnotation) {
                    annotationItems.add(data)
                }
            }
        }

        return ProductDetailInfoHeaderDataModel(
            componentId = componentId,
            img = parcelData.productImageUrl,
            productTitle = parcelData.productTitle,
            listOfInfo = productInfoItems,
            listOfAnnotation = annotationItems
        )
    }


    private fun isInfoUnderMax(currentSize: Int) = currentSize < SPECIFICATION_SIZE_THRESHOLD
}