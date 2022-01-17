package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.track.TrackApp


data class ProductArTrackerData(
        val productInfo: DynamicProductInfoP1?,
        val componentTrackDataModel: ComponentTrackDataModel
) {
    private val productBasic = productInfo?.basic
    private val basicCategory = productBasic?.category

    val componentName = componentTrackDataModel.componentName
    val componentType = componentTrackDataModel.componentType
    val componentPosition = componentTrackDataModel.adapterPosition.toString()

    val layoutName = productInfo?.layoutName ?: ""
    val categoryName = basicCategory?.name ?: ""
    val categoryId = basicCategory?.id ?: ""
    val productId = productBasic?.productID ?: ""
}

object ProductArTracking {

    private const val ACTION_CLICK_AR_COMPONENT = "click - cobain barangnya yuk"
    private const val LABEL_AR_COMPONENT = "feature:product AR"

    fun clickArComponent(data: ProductArTrackerData) {
        val componentName = data.componentName
        val componentPosition = data.componentPosition
        val componentType = data.componentType

        val mapEvent = mapOf(
                "event" to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "eventAction" to ACTION_CLICK_AR_COMPONENT,
                "eventCategory" to ProductTrackingConstant.Category.PDP,
                "eventLabel" to LABEL_AR_COMPONENT,
                "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
                "component" to "'comp:$componentName;temp:$componentType;elem:${ACTION_CLICK_AR_COMPONENT};cpos:$componentPosition;",
                "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
                "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
                "productId" to data.productId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}