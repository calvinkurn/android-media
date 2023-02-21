package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.tracking.TrackingConstant.Hit
import com.tokopedia.track.TrackApp

object GeneralInfoTracking {

    fun onClickObatKeras(
        common: CommonTracker,
        data: GeneralInfoTracker
    ) {
        val eventAction = "click - obat keras component - lihat on perlu resep dokter"
        val pdp = "product detail page"

        val mapEvent = hashMapOf<String, Any>(
            Hit.EVENT to "clickPG",
            Hit.EVENT_ACTION to eventAction,
            Hit.EVENT_CATEGORY to pdp,
            Hit.EVENT_LABEL to "product_id:${common.productId};user_id:${common.userId};shop_id:${common.shopId};category:${common.categoryChildId};isTokonow:${data.isTokoNow};",
            Hit.TRACKER_ID to "39383",
            Hit.BUSINESS_UNIT to pdp,
            Hit.CATEGORY_ID to common.categoryId,
            Hit.COMPONENT to "comp:${data.componentName};temp:${data.componentType};elem:$eventAction;cpos:${data.componentPosition};",
            Hit.CURRENT_SITE to "tokopediamarketplace",
            Hit.LAYOUT to "layout:${common.layoutName};catName:${common.categoryName};catId:${common.categoryId};",
            Hit.PRODUCT_ID to common.productId,
            Hit.SHOP_ID to common.shopId,
            Hit.SHOP_TYPE to common.shopType,
            Hit.USER_ID to common.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
