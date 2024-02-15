package com.tokopedia.editor.util

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.utils.file.FileUtil
import java.io.File

private const val CACHE_FOLDER = "Tokopedia/editor-stories"
private var FILE_AGE_LIMIT = 259_200_000 // 3 days

internal fun getEditorCacheFolderPath(): String {
    return FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path + "/"
}

internal fun View.slideTop(): ObjectAnimator {
    return this.animateSlide(-(this.height + this.y.toInt()))
}

internal fun View.slideDown(): ObjectAnimator {
    val totalHeight = this.marginBottom + this.marginTop + this.height
    return this.animateSlide(totalHeight)
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

internal suspend fun clearEditorCache() {
    val rootFolder = File(getEditorCacheFolderPath())
    val now = System.currentTimeMillis()
    rootFolder.listFiles()?.forEach {
        if (!it.exists()) return@forEach
        if (now - it.lastModified() >= FILE_AGE_LIMIT) deleteFile(it)
    }
}

/**
 * Delete target file, true if success
 */
private suspend fun deleteFile(targetFile: File): Boolean {
    if (!targetFile.exists()) return false

    return try {
        targetFile.delete()
    } catch (_: Exception) {
        false
    }
}
