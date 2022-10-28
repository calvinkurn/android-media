package com.tokopedia.product.info.util

import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.data.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.util.ProductDetailInfoConstant.CATALOG
import com.tokopedia.product.info.util.ProductDetailInfoConstant.CUSTOM_INFO_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.GUIDELINE_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.HEADER_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.SHOP_NOTES_DETAIL_KEY
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoAnnotationDataModel.Companion.SPECIFICATION_SIZE_THRESHOLD
import com.tokopedia.product.info.view.models.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.view.models.ProductDetailInfoVisitable

/**
 * Created by Yehezkiel on 20/10/20
 */
object ProductDetailInfoMapper {

    fun generateVisitable(
        responseData: PdpGetDetailBottomSheet,
        parcelData: ProductInfoParcelData
    ): List<ProductDetailInfoVisitable> {
        val components = generateComponents(
            responseData = responseData,
            parcelData = parcelData
        )

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

    private fun generateComponents(
        responseData: PdpGetDetailBottomSheet,
        parcelData: ProductInfoParcelData
    ): MutableList<ProductDetailInfoVisitable> {
        val listOfComponent: MutableList<ProductDetailInfoVisitable> = mutableListOf()
        val dataContent = parcelData.productInfo.dataContent

        responseData.bottomsheetData.forEachIndexed { index, it ->
            when (it.componentName) {
                HEADER_KEY -> {
                    val header = ProductDetailInfoHeaderDataModel(
                        componentId = index,
                        image = parcelData.productImageUrl,
                        title = parcelData.productTitle
                    )
                    listOfComponent.add(header)
                }
                DETAIL_KEY -> listOfComponent.add(
                    generateDetailAnnotations(componentId = index, parcelData = parcelData)
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
                    listOfComponent.add(
                        ProductDetailInfoCatalogDataModel(
                            componentName = index,
                            title = it.title,
                            items = it.row
                        )
                    )
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

    private fun generateDetailAnnotations(
        componentId: Int,
        parcelData: ProductInfoParcelData
    ): ProductDetailInfoAnnotationDataModel {
        val dataContent = parcelData.productInfo.dataContent
        val productInfo = dataContent.filter {
            it.title.lowercase() != DESCRIPTION_DETAIL_KEY && it.showAtBottomSheet
        }
        val annotation = if (productInfo.size > SPECIFICATION_SIZE_THRESHOLD) {
            productInfo.subList(SPECIFICATION_SIZE_THRESHOLD, productInfo.size)
        } else {
            emptyList()
        }

        return ProductDetailInfoAnnotationDataModel(
            componentId = componentId,
            productInfo = productInfo.take(SPECIFICATION_SIZE_THRESHOLD),
            annotation = annotation
        )
    }
}
