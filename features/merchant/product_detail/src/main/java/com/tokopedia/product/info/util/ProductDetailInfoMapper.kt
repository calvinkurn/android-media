package com.tokopedia.product.info.util

import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.model.specification.Row
import com.tokopedia.product.info.model.specification.Specification
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.GUIDELINE_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.HEADER_DETAIL_KEY
import com.tokopedia.product.info.util.ProductDetailInfoConstant.SHOP_NOTES_DETAIL_KEY

/**
 * Created by Yehezkiel on 20/10/20
 */
object ProductDetailInfoMapper {

    private const val MAXIMUM_LIST_SHOWING = 8

    fun generateVisitable(responseData: PdpGetDetailBottomSheet, parcelData: ProductInfoParcelData): List<ProductDetailInfoVisitable> {
        val listOfComponent: MutableList<ProductDetailInfoVisitable> = mutableListOf()
        responseData.bottomsheetData.forEachIndexed { index, it ->
            when (it.componentName) {
                HEADER_DETAIL_KEY -> {
                    val productInfoData = parcelData.data.filter { it.title.toLowerCase() != DESCRIPTION_DETAIL_KEY }.take(MAXIMUM_LIST_SHOWING)
                    val productSpecificationData = responseData.specification.catalog.specification.take(MAXIMUM_LIST_SHOWING - productInfoData.count()).map {
                        ProductDetailInfoContent(title = it.row.firstOrNull()?.key
                                ?: "", subtitle = it.row.firstOrNull()?.value?.firstOrNull()
                                ?: "", applink = "")
                    }

                    val listOfSpecification = responseData.specification.catalog.specification.toMutableList()

                    // If the data and annotation is more than 8,
                    // we need to append the annotation to the specification tab
                    if (productInfoData.size > 8) {
                        listOfSpecification.addAll(0, productInfoData.filter { it.isAnnotation }.map {
                            Specification("", listOf(Row(it.title, listOf(it.subtitle))))
                        })
                    }

                    val appendedData = productInfoData + productSpecificationData
                    listOfComponent.add(ProductDetailInfoHeaderDataModel(index, parcelData.productImageUrl, parcelData.productTitle, appendedData, responseData.specification.catalog.specification))
                }
                DESCRIPTION_DETAIL_KEY -> {
                    val descriptionValue = parcelData.data.firstOrNull { it.title.toLowerCase() == DESCRIPTION_DETAIL_KEY }?.subtitle

                    if (descriptionValue?.isNotEmpty() == true) {
                        listOfComponent.add(ProductDetailInfoExpandableDataModel(index, it.title, descriptionValue, parcelData.listOfVideo, it.isShowable))
                    }
                }
                GUIDELINE_DETAIL_KEY -> {
                    val variantGuideline = parcelData.variantGuideline
                    if (variantGuideline.isNotEmpty()) {
                        listOfComponent.add(ProductDetailInfoExpandableImageDataModel(index, it.title, variantGuideline, it.isShowable))
                    }
                }
                SHOP_NOTES_DETAIL_KEY -> {
                    val shopNotes = responseData.dataShopNotes
                    if (shopNotes.error.isEmpty() && shopNotes.shopNotesData.isNotEmpty()) {
                        listOfComponent.add(ProductDetailInfoExpandableListDataModel(index, it.title, responseData.dataShopNotes.shopNotesData, it.isShowable))
                    }
                }
                else -> {
                    if (it.value.isNotEmpty()) {
                        listOfComponent.add(ProductDetailInfoExpandableDataModel(index, it.title, it.value, listOf(), it.isShowable))
                    }
                }
            }
        }

        listOfComponent.add(ProductDetailInfoDiscussionDataModel(componentName = listOfComponent.count() + 1, title = responseData.discussion.title, discussionCount = parcelData.discussionCount, isShowable = false))

        return listOfComponent
    }
}