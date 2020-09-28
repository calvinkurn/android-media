package com.tokopedia.play.broadcaster.util.cover

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by jegul on 18/06/20
 */
interface PlayCoverImageUtil {

    fun getImagePathFromBitmap(image: Bitmap): Uri
}