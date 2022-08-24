package com.tokopedia.tokopedianow.common.util

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify

object BottomSheetUtil {
    internal fun BottomSheetUnify.configureBottomSheetHeight() {
        val screenHeight = getScreenHeight()
        val maxHeight = (screenHeight * 0.9f).toInt()
        bottomSheetWrapper.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
    }
}