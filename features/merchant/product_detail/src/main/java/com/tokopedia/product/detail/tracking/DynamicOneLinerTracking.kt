package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.track.TrackApp

object DynamicOneLinerTracking {
    fun onClickDynamicOneliner(
        title: String,
        data: CommonTracker,
        componentData: ComponentTrackDataModel
    ) {
        val componentName = componentData.componentName
        val componentType = componentData.componentType
        val componentPosition = componentData.adapterPosition

        val eventAction = "click - dynamic one liner"

        val mapEvent = hashMapOf<String, Any>(
            "event" to "clickPG",
            "eventAction" to eventAction,
            "eventCategory" to "product detail page",
            "eventLabel" to "text:$title;",
            "trackerId" to "44806",
            "businessUnit" to "product detail page",
            "component" to "'comp:$componentName;temp:$componentType;elem:$eventAction;cpos:$componentPosition",
            "currentSite" to "tokopediamarketplace",
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId}",
            "productId" to data.productId,
            "shopId" to data.shopId,
            "userId" to data.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
