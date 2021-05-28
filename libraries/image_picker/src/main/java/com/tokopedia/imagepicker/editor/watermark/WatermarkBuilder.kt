package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.imagepicker.editor.watermark.listener.BuildFinishListener
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkImage
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText

data class WatermarkBuilder(
    var context: Context,
    var backgroundImg: Bitmap,
    var isTitleMode: Boolean = false,
    var resizeBackgroundImg: Boolean,
    val finishListener: BuildFinishListener<Bitmap>? = null,
    var watermarkImage: WatermarkImage,
    var watermarkText: WatermarkText,
    val watermarkTexts: List<WatermarkText> = arrayListOf(),
    val watermarkBitmaps: List<WatermarkImage> = arrayListOf()
) {



}