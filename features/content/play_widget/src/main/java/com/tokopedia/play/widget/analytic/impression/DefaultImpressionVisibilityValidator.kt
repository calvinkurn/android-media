package com.tokopedia.play.widget.analytic.impression

import android.graphics.Rect
import android.view.View
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth

/**
 * Created by jegul on 28/10/20
 */
class DefaultImpressionVisibilityValidator(
        private val offset: Int = 100
) : ImpressionVisibilityValidator {

    override fun isViewVisibleForImpression(view: View): Boolean {
        if (!view.isShown) return false
        val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val X = location[0] + offset
        val Y = location[1] + offset
        return screen.top <= Y && screen.bottom >= Y &&
                screen.left <= X && screen.right >= X
    }
}