package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionParams
import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
import com.tokopedia.variant_common.model.ProductVariantCommon

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): MutableList<DynamicPdpDataModel> {
        val listOfComponent: MutableList<DynamicPdpDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                ProductDetailConstant.PRODUCT_SNAPSHOT -> {
                    listOfComponent.add(ProductSnapshotDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.NOTIFY_ME -> {
                    listOfComponent.add(ProductNotifyMeDataModel(
                            type = component.type,
                            name = component.componentName,
                            campaignID = component.componentData.firstOrNull()?.campaignId ?: "",
                            campaignType = component.componentData.firstOrNull()?.campaignType
                                    ?: "",
                            campaignTypeName = component.componentData.firstOrNull()?.campaignTypeName
                                    ?: "",
                            endDate = component.componentData.firstOrNull()?.endDate ?: "",
                            startDate = component.componentData.firstOrNull()?.startDate ?: "",
                            notifyMe = component.componentData.firstOrNull()?.notifyMe ?: false
                    ))
                }
                ProductDetailConstant.DISCUSSION -> {
                    listOfComponent.add(ProductDiscussionDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.PRODUCT_INFO -> {
                    listOfComponent.add(ProductInfoDataModel(mapToProductInfoContent(component.componentData), type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SHOP_INFO -> {
                    listOfComponent.add(ProductShopInfoDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.SOCIAL_PROOF -> {
                    if (component.componentName == ProductDetailConstant.SOCIAL_PROOF_PV) {
                        listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName, isSocialProofPv = true))
                    } else {
                        listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName, isSocialProofPv = false))
                    }
                }
                ProductDetailConstant.MOST_HELPFUL_REVIEW -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.INFO -> {
                    val contentData = component.componentData.firstOrNull()
                    val content = if (contentData?.content?.isEmpty() == true) listOf(Content()) else contentData?.content
                    listOfComponent.add(ProductGeneralInfoDataModel(component.componentName, component.type, contentData?.applink
                            ?: "", contentData?.title ?: "",
                            contentData?.isApplink ?: true, contentData?.icon
                            ?: "", content ?: listOf(Content()))
                    )
                }
                ProductDetailConstant.PRODUCT_LIST -> {
                    listOfComponent.add(ProductRecommendationDataModel(type = component.type, name = component.componentName, position = index))
                }
                ProductDetailConstant.SHOP_VOUCHER -> {
                    listOfComponent.add(ProductMerchantVoucherDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.VALUE_PROPOSITION -> {
                    listOfComponent.add(ProductValuePropositionDataModel(type = component.type, name = component.componentName))
                }
                ProductDetailConstant.VARIANT -> {
                    listOfComponent.add(VariantDataModel(type = component.type, name = component.componentName))
                }
            }
        }
        return listOfComponent
    }

    fun mapToDynamicProductDetailP1(data: PdpGetLayout): DynamicProductInfoP1 {
        val componentData = data.components.find {
            it.type == ProductDetailConstant.PRODUCT_SNAPSHOT
        }?.componentData?.firstOrNull() ?: ComponentData()

        val upcomingData = data.components.find {
            it.type == ProductDetailConstant.NOTIFY_ME
        }?.componentData?.firstOrNull() ?: ComponentData()

        val newData = componentData.copy(
                campaignId = upcomingData.campaignId,
                campaignType = upcomingData.campaignType,
                campaignTypeName = upcomingData.campaignTypeName,
                endDate = upcomingData.endDate,
                startDate = upcomingData.startDate,
                notifyMe = upcomingData.notifyMe
        )

        return DynamicProductInfoP1(layoutName = data.generalName, basic = data.basicInfo, data = newData)
    }

    fun hashMapLayout(data: List<DynamicPdpDataModel>): Map<String, DynamicPdpDataModel> {
        return data.associateBy({
            it.name()
        }, {
            it
        })
    }

    fun generateCartTypeVariantParams(dynamicProductInfoP1: DynamicProductInfoP1?, productVariant: ProductVariantCommon?): List<CartRedirectionParams> {

        return productVariant?.children?.map {
            val listOfFlags = mutableListOf<String>()
            if (dynamicProductInfoP1?.data?.preOrder?.isActive == true) listOfFlags.add(ProductDetailConstant.KEY_PREORDER)
            if (dynamicProductInfoP1?.basic?.isLeasing == true) listOfFlags.add(ProductDetailConstant.KEY_LEASING)
            if (it.campaign?.isUsingOvo == true) listOfFlags.add(ProductDetailConstant.KEY_OVO_DEALS)

            CartRedirectionParams(it.campaign?.campaignID?.toIntOrNull() ?: 0,
                    it.campaign?.campaignType ?: 0, listOfFlags)
        } ?: listOf()
    }

    fun generateCartTypeParam(dynamicProductInfoP1: DynamicProductInfoP1?): List<CartRedirectionParams> {
        val campaignId = dynamicProductInfoP1?.data?.campaign?.campaignID?.toIntOrNull() ?: 0
        val campaignTypeId = dynamicProductInfoP1?.data?.campaign?.campaignType?.toIntOrNull() ?: 0
        val listOfFlags = mutableListOf<String>()
        if (dynamicProductInfoP1?.data?.preOrder?.isActive == true) listOfFlags.add(ProductDetailConstant.KEY_PREORDER)
        if (dynamicProductInfoP1?.basic?.isLeasing == true) listOfFlags.add(ProductDetailConstant.KEY_LEASING)
        if (dynamicProductInfoP1?.data?.campaign?.isUsingOvo == true) listOfFlags.add(ProductDetailConstant.KEY_OVO_DEALS)

        return listOf(CartRedirectionParams(campaignId, campaignTypeId, listOfFlags))
    }

    fun generateButtonAction(it: String, atcButton: Boolean, leasing: Boolean): Int {
        return when {
            atcButton -> ProductDetailConstant.ATC_BUTTON
            leasing -> ProductDetailConstant.LEASING_BUTTON
            it == ProductDetailConstant.KEY_NORMAL_BUTTON -> {
                ProductDetailConstant.BUY_BUTTON
            }
            it == ProductDetailConstant.KEY_OCS_BUTTON -> {
                ProductDetailConstant.OCS_BUTTON
            }
            it == ProductDetailConstant.KEY_OCC_BUTTON -> {
                ProductDetailConstant.OCC_BUTTON
            }
            else -> ProductDetailConstant.BUY_BUTTON
        }
    }

    fun mapToWholesale(data: List<Wholesale>?): List<com.tokopedia.product.detail.common.data.model.product.Wholesale>? {
        return if (data == null || data.isEmpty()) {
            null
        } else {
            data.map {
                com.tokopedia.product.detail.common.data.model.product.Wholesale(it.minQty, it.price.value.toFloat())
            }
        }
    }

    fun convertMediaToDataModel(media: MutableList<Media>): List<ProductMediaDataModel> {
        return media.map { it ->
            ProductMediaDataModel(it.type, it.uRL300, it.uRLOriginal, it.uRLThumbnail, it.description, it.videoURLAndroid, it.isAutoplay)
        }
    }

    private fun mapToProductInfoContent(listOfData: List<ComponentData>): List<ProductInfoContent>? {
        return if (listOfData.isEmpty()) {
            null
        } else {
            listOfData.map {
                ProductInfoContent(it.row, it.content)
            }
        }
    }
}