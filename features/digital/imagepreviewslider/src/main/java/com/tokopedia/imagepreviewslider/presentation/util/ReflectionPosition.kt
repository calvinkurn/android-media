package com.tokopedia.imagepreviewslider.presentation.util

import android.util.Log
import com.stfalcon.imageviewer.StfalconImageViewer

/**
 * @author by jessica on 2019-12-16
 */

@Deprecated(level = DeprecationLevel.WARNING,
        message = "This should be replaced by adding setCurrentPosition in StfalconImageViewer and ImageViewerDialog to set the current position for imagesPager. " +
                "This is a temporary solution to set the current position or currentItem of imagesPager.currentItem in ImageViewerView")
internal class ReflectionPosition<T>(viewer: StfalconImageViewer<T>, position: Int) {
    init {
        try {
            val field = viewer.javaClass.getDeclaredField("dialog")
            field.isAccessible = true
            val viewerViewField = field.type.getDeclaredField("viewerView")
            viewerViewField.isAccessible = true
            val method = viewerViewField.type.methods.first { it.name.contains("setCurrentPosition", true) }

            method.invoke(viewerViewField.get(field.get(viewer)), position)

        } catch (e: Exception) {
            Log.e("ReflectionPosition", e.message.toString())
        }
    }
}