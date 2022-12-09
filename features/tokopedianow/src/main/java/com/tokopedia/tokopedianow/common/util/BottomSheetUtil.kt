package com.tokopedia.tokopedianow.common.util

import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify

object BottomSheetUtil {
    internal fun BottomSheetUnify.configureMaxHeight(maxHeightNeeded: Boolean = false) {
        val screenHeight = getScreenHeight()
        customPeekHeight = screenHeight / 2

        setShowListener {
            var maxHeight = (screenHeight * 0.9f).toInt()
            val bottomSheetHeight = bottomSheetWrapper.height

            maxHeight = if (!maxHeightNeeded && bottomSheetHeight < maxHeight) {
                bottomSheetHeight
            } else {
                maxHeight
            }

            bottomSheetWrapper.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                maxHeight
            )
        }
    }

    internal fun BottomSheetUnify.setMaxHeight() {
        val maxHeight = (getScreenHeight() * 0.9f).toInt()

        bottomSheetWrapper.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            maxHeight
        )
    }
}