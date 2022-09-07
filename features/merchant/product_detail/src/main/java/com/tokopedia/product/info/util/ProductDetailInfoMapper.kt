package com.tokopedia.product.info.util

import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoCardDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoCatalogDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoHeaderDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable
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
        val listOfComponent: MutableList<ProductDetailInfoVisitable> = mutableListOf()
        val dataContent = parcelData.productInfo.dataContent
        responseData.bottomsheetData.forEachIndexed { index, it ->
            when (it.componentName) {
                HEADER_DETAIL_KEY -> {
                    val productInfoData = dataContent.filter {
                        it.title.lowercase() != DESCRIPTION_DETAIL_KEY
                    }.take(ProductDetailInfoHeaderDataModel.SPECIFICATION_SIZE_THRESHOLD)
                    val annotationData = dataContent.filter {
                        it.title.lowercase() != DESCRIPTION_DETAIL_KEY && it.isAnnotation
                    }

                    listOfComponent.add(
                        ProductDetailInfoHeaderDataModel(
                            index,
                            parcelData.productImageUrl,
                            parcelData.productTitle,
                            productInfoData,
                            annotationData
                        )
                    )
                }
                DESCRIPTION_DETAIL_KEY -> {
                    val descriptionValue =
                        dataContent.firstOrNull { it.title.lowercase() == DESCRIPTION_DETAIL_KEY }?.subtitle

                    if (descriptionValue?.isNotEmpty() == true) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableDataModel(
                                index,
                                it.title,
                                descriptionValue,
                                parcelData.listOfYoutubeVideo,
                                it.isShowable
                            )
                        )
                    }
                }
                GUIDELINE_DETAIL_KEY -> {
                    val variantGuideline = parcelData.variantGuideline
                    if (variantGuideline.isNotEmpty()) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableImageDataModel(
                                index,
                                it.title,
                                variantGuideline,
                                it.isShowable
                            )
                        )
                    }
                }
                SHOP_NOTES_DETAIL_KEY -> {
                    val shopNotes = responseData.dataShopNotes
                    if (shopNotes.error.isEmpty() && shopNotes.shopNotesData.isNotEmpty()) {
                        listOfComponent.add(
                            ProductDetailInfoExpandableListDataModel(
                                index,
                                it.title,
                                responseData.dataShopNotes.shopNotesData,
                                it.isShowable
                            )
                        )
                    }
                }
                CUSTOM_INFO_KEY -> {
                    listOfComponent.add(
                        ProductDetailInfoCardDataModel(
                            index,
                            it.title,
                            it.icon,
                            it.applink
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
                                index,
                                it.title,
                                it.value,
                                listOf(),
                                it.isShowable
                            )
                        )
                    }
                }
            }
        }

        if (!parcelData.isTokoNow) {
            listOfComponent.add(
                ProductDetailInfoDiscussionDataModel(
                    componentName = listOfComponent.count() + 1,
                    title = responseData.discussion.title,
                    discussionCount = parcelData.discussionCount,
                    isShowable = false
                )
            )
        }
        return listOfComponent
    }
}