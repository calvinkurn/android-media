package com.tokopedia.media.editor.ui.uimodel

import android.graphics.RectF

class EditorRotateModel (
    var rotateDegree: Float,
    var scaleX: Float,
    var scaleY: Float,
    var leftRectPos: Int = 0,
    var topRectPos: Int = 0,
    var rightRectPos: Int = 0,
    var bottomRectPos: Int = 0,
) {
    fun getCropRect(): RectF{
        return RectF(
            leftRectPos.toFloat(),
            topRectPos.toFloat(),
            rightRectPos.toFloat(),
            bottomRectPos.toFloat()
        )
    }
}