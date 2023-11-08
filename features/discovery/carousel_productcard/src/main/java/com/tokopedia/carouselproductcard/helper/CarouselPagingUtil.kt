package com.tokopedia.carouselproductcard.helper

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.unifycomponents.toPx

internal class CarouselPagingUtil (
    private val context: Context,
    private val itemWidthPercentage: Float,
    private val padding: Int,
) {
    private val screenWidth by lazy { DeviceScreenInfo.getScreenWidth(context) }

    fun getItemWidth(): Int {
        return try {
            ((screenWidth * itemWidthPercentage) - padding).toInt()
        } catch (_: Throwable) {
            DEFAULT_ITEM_WIDTH_DP.toPx()
        }
    }

    fun getRemainingScreenSize(): Int {
        return (screenWidth * (1 - itemWidthPercentage)).toInt()
    }

    companion object {
        private const val DEFAULT_ITEM_WIDTH_DP = 300
    }
}
