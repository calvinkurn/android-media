package com.tokopedia.play_common.view

import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play_common.util.extension.doOnLayout

/**
 * Created by kenny.hadisaputra on 29/07/22
 */
@RequiresApi(Build.VERSION_CODES.R)
fun WindowInsets.getImeHeight(): Int {
    return getInsets(WindowInsets.Type.ime()).bottom
}

fun View.getImeHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        rootWindowInsets?.getImeHeight().orZero()
    } else {
        ViewCompat.getRootWindowInsets(this)
            ?.systemWindowInsets?.bottom.orZero()
    }
}

fun View.isImeVisible(threshold: Int = 200): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        rootWindowInsets?.isVisible(WindowInsets.Type.ime()) == true
    } else {
        getImeHeight() > threshold
    }
}

fun View.addKeyboardInsetsListener(
    triggerOnAttached: Boolean = true,
    heightThreshold: (View) -> Int = { (0.3f * it.height).toInt() },
    onVisibilityChanged: (isVisible: Boolean, height: Int) -> Unit,
) {
    doOnLayout {
        val threshold = heightThreshold(it)
        var isVisible = isImeVisible(threshold)
        var height = getImeHeight()

        if (triggerOnAttached) onVisibilityChanged(isVisible, height)

        setOnApplyWindowInsetsListener { _, insets->

            val imeHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getImeHeight()
            } else insets.systemWindowInsetBottom

            val isImeVisible = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.isVisible(WindowInsets.Type.ime())
            } else imeHeight > threshold

            if (isVisible != isImeVisible || height != imeHeight) {
                onVisibilityChanged(isImeVisible, imeHeight)
                isVisible = isImeVisible
                height = imeHeight
            }

            insets
        }
    }
}