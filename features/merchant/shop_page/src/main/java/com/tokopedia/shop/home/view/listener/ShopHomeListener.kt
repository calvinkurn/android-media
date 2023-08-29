package com.tokopedia.shop.home.view.listener

import android.util.SparseIntArray
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.trackingoptimizer.TrackingQueue

interface ShopHomeListener {
    fun getWidgetCarouselPositionSavedState(): SparseIntArray

    fun getFragmentTrackingQueue(): TrackingQueue?

    fun getShopPageColorSchema(): ShopPageColorSchema

    fun isOverrideTheme(): Boolean

    fun getShopPageHomeFragment(): ShopPageHomeFragment

    fun getPatternColorType(): String

}
