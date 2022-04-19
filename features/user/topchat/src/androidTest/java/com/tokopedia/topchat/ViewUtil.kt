package com.tokopedia.topchat

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import com.tokopedia.chat_common.view.BaseChatViewStateImpl

fun isKeyboardOpened(rootView: View): Boolean {
    val screenHeight = getScreenHeight()
    val windowRect = Rect().apply {
        rootView.getWindowVisibleDisplayFrame(this)
    }
    val windowHeight = windowRect.bottom - windowRect.top
    val statusBarHeight = getStatusBarHeight(rootView.context)
    val heightDifference = screenHeight - windowHeight - statusBarHeight
    return heightDifference > BaseChatViewStateImpl.KEYBOARD_OFFSET
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun getStatusBarHeight(context: Context): Int {
    var height = 0
    val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
    )
    if (resourceId > 0) {
        height = context.resources.getDimensionPixelSize(resourceId)
    }
    return height
}