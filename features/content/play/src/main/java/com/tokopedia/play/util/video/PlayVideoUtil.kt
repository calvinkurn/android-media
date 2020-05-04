package com.tokopedia.play.util.video

import android.graphics.Bitmap

/**
 * Created by jegul on 30/04/20
 */
interface PlayVideoUtil {

    fun saveEndImage(image: Bitmap)

    fun getEndImage(): Bitmap?

    fun clearImage()
}