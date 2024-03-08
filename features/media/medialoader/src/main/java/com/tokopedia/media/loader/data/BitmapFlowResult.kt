package com.tokopedia.media.loader.data

import android.graphics.Bitmap

sealed interface BitmapFlowResult {
    data class Success(val bitmap: Bitmap) : BitmapFlowResult
    data class Failed(val throwable: Throwable?) : BitmapFlowResult
}
