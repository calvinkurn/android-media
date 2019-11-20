package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.Component
import com.tokopedia.product.detail.data.model.datamodel.*

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): List<DynamicPDPDataModel> {
        val listOfComponent: MutableList<DynamicPDPDataModel> = mutableListOf()
        data.forEachIndexed { _, component ->
            when (component.type) {
                "product_snapshot" -> {
                    listOfComponent.add(ProductSnapshotDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "discussion" -> {
                    listOfComponent.add(ProductDiscussionDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "product_info" -> {
                    listOfComponent.add(ProductInfoDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "shop_info" -> {
                    listOfComponent.add(ProductShopInfoDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "social_proof" -> {
                    listOfComponent.add(ProductSocialProofDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "image_review" -> {
                    listOfComponent.add(ProductImageReviewDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "most_helpful_review" -> {
                    listOfComponent.add(ProductMostHelpfulReviewDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "trade_in" -> {
                    listOfComponent.add(ProductTradeinDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
                "info" -> {
                    listOfComponent.add(ProductGeneralInfoDataModel(dataLayout = component.componentData, type = component.type, name = component.componentName))
                }
            }
        }
        return listOfComponent
    }

    fun hashMapLayout(data: List<DynamicPDPDataModel>): Map<String, DynamicPDPDataModel> {
        return data.associateBy({
            if (it.type() != it.name()) it.type() else it.name()
        }, {
            it
        })
    }
}