package com.tokopedia.media.editor.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject
import com.tokopedia.media.editor.R as editorR

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    fun getWatermarkLogoDrawable(): Drawable? {
        return getDrawable(editorR.drawable.watermark_tokopedia)
    }

    fun getWatermarkTextColor(): Pair<Int?, Int?> {
        return Pair(
            getColor(editorR.color.dms_watermark_text_light),
            getColor(editorR.color.dms_watermark_text_dark)
        )
    }

    private fun getDrawable(resId: Int): Drawable? {
        return context?.let {
            ContextCompat.getDrawable(it, resId)
        } ?: run {
            null
        }
    }

    private fun getColor(resId: Int): Int? {
        return context?.let {
            ContextCompat.getColor(it, resId)
        } ?: run {
            null
        }
    }

    private fun getDimension(resId: Int): Float? {
        return context?.resources?.getDimension(resId) ?: run {
            null
        }
    }
}