package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

data class CommonTracker(
    private val productInfo: DynamicProductInfoP1,
    private val componentTrackDataModel: ComponentTrackDataModel,
    val userId: String
) {
    private val productBasic = productInfo.basic
    private val basicCategory = productBasic.category

    val shopId = productBasic.shopID
    val componentName = componentTrackDataModel.componentName
    val componentType = componentTrackDataModel.componentType
    val componentPosition = componentTrackDataModel.adapterPosition.toString()
    val layoutName = productInfo.layoutName
    val categoryName = basicCategory.name
    val categoryId = basicCategory.id
    val productId = productBasic.productID
    val shopType = productInfo.shopTypeString
}
