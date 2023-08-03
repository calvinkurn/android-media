package com.tokopedia.search.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceScreenInfo
import javax.inject.Inject

class SmallGridSpanCount @Inject constructor(@ApplicationContext private val context: Context?) {
    companion object {
        private const val COMPACT_SCREEN_BREAKPOINT = 580f
        private const val MEDIUM_SCREEN_BREAKPOINT = 840f
        private const val COMPACT_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT = 2
        private const val MEDIUM_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT = 3
        private const val EXPANDED_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT = 4
    }

    operator fun invoke(): Int {
        val context = context ?: return COMPACT_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT

        val screenDensity = context.resources.displayMetrics.density
        val screenWidthInDp = DeviceScreenInfo.getScreenWidth(context) / screenDensity
        return when {
            screenWidthInDp < COMPACT_SCREEN_BREAKPOINT -> COMPACT_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT
            screenWidthInDp < MEDIUM_SCREEN_BREAKPOINT -> MEDIUM_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT
            else -> EXPANDED_SCREEN_SMALL_GRID_LAYOUT_SPAN_COUNT
        }
    }
}
