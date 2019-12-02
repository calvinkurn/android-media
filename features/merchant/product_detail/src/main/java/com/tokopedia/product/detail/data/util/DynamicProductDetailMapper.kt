package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.data.model.datamodel.*

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): MutableList<DynamicPDPDataModel> {
        val listOfComponent: MutableList<DynamicPDPDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                "product_snapshot" -> {
                    listOfComponent.add(ProductSnapshotDataModel(type = component.type, name = component.componentName))
                }
                "discussion" -> {
                    listOfComponent.add(ProductDiscussionDataModel(type = component.type, name = component.componentName))
                }
                "product_info" -> {
                    listOfComponent.add(ProductInfoDataModel(mapToProductInfoContent(component.componentData), type = component.type, name = component.componentName))
                }
                "shop_info" -> {
                    listOfComponent.add(ProductShopInfoDataModel(type = component.type, name = component.componentName))
                }
                "social_proof" -> {
                    listOfComponent.add(ProductSocialProofDataModel(type = component.type, name = component.componentName))
                }
                "image_review" -> {
                    listOfComponent.add(ProductImageReviewDataModel(type = component.type, name = component.componentName))
                }
                "most_helpful_review" -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(type = component.type, name = component.componentName))
                }
                "trade_in" -> {
                    listOfComponent.add(ProductTradeinDataModel(type = component.type, name = component.componentName))
                }
                "info" -> {
                    val data = component.componentData.firstOrNull()
                    listOfComponent.add(ProductGeneralInfoDataModel(ProductGeneralInfoData(data?.applink
                            ?: "", data?.title ?: "", data?.content
                            ?: listOf()), type = component.type, name = component.componentName))
                }
                "product_list" -> {
                    listOfComponent.add(ProductRecommendationDataModel(type = component.type, name = component.componentName, position = index))
                }
                "shop_voucher" -> {
                    listOfComponent.add(ProductMerchantVoucherDataModel(type = component.type, name = component.componentName))
                }
            }
        }
        return listOfComponent
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

    fun mapToDynamicProductDetailP1(data: PdpGetLayout): DynamicProductInfoP1 {
        val componentData = data.components.find {
            it.type == "product_snapshot"
        }?.componentData?.firstOrNull() ?: ComponentData()

        return DynamicProductInfoP1(basic = data.basicInfo, data = componentData)
    }

    fun hashMapLayout(data: List<DynamicPDPDataModel>): Map<String, DynamicPDPDataModel> {
        return data.associateBy({
            if (it.type() != it.name()) it.name() else it.type()
        }, {
            it
        })
    }
}