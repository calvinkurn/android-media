package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): MutableList<DynamicPdpDataModel> {
        val listOfComponent: MutableList<DynamicPdpDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                ProductDetailConstant.PRODUCT_SNAPSHOT -> {
                    listOfComponent.add(ProductSnapshotDataModel(type = component.type, name = component.componentName))
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
                    listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName))
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
                ProductDetailConstant.SEPARATOR -> {
                    listOfComponent.add(SeparatorDataModel(type = component.type, name = component.componentName))
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

        return DynamicProductInfoP1(layoutName = data.generalName, basic = data.basicInfo, data = componentData)
    }

    fun hashMapLayout(data: List<DynamicPdpDataModel>): Map<String, DynamicPdpDataModel> {
        return data.associateBy({
            it.name()
        }, {
            it
        })
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