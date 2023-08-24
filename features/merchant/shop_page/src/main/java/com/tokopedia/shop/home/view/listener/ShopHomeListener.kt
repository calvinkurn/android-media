package com.tokopedia.shop.home.view.listener

import android.util.SparseIntArray
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.trackingoptimizer.TrackingQueue

interface ShopHomeListener {
    fun getWidgetCarouselPositionSavedState(): SparseIntArray

    fun getFragmentTrackingQueue(): TrackingQueue?

    fun isShopHomeTabHasFestivity(): Boolean

    fun getShopPageColorSchema(): ShopPageColorSchema

    fun isOverrideTheme(): Boolean

}
