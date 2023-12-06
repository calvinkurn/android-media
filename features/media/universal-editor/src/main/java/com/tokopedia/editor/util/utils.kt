package com.tokopedia.editor.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.file.FileUtil

private const val CACHE_FOLDER = "Tokopedia/editor-stories"

internal fun getEditorCacheFolderPath(): String {
    return FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path + "/"
}

internal fun View.slideTop(): ObjectAnimator {
    return this.animateSlide(-(this.height + this.y.toInt()))
}

internal fun View.slideDown(): ObjectAnimator {
    return this.animateSlide(this.height)
}

internal fun View.slideOriginalPos(): ObjectAnimator {
    return this.animateSlide(0)
}

internal fun View.animateSlide(yPosTarget: Int): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationY", yPosTarget.toFloat()).apply {
        repeatCount = 0
        interpolator = UnifyMotion.EASE_OVERSHOOT
    }
}
