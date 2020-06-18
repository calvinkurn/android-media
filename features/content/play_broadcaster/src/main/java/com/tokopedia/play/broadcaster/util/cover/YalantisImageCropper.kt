package com.tokopedia.play.broadcaster.util.cover

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import com.yalantis.ucrop.model.ExifInfo

/**
 * Created by jegul on 18/06/20
 */
interface YalantisImageCropper {

    suspend fun cropImage(
            inputPath: String,
            cropRect: RectF,
            currentRect: RectF,
            currentScale: Float,
            currentAngle: Float,
            exifInfo: ExifInfo,
            viewBitmap: Bitmap
    ): Uri
}