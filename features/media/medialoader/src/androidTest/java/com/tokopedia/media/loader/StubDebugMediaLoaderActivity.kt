package com.tokopedia.media.loader

import android.graphics.Bitmap

class StubDebugMediaLoaderActivity : DebugMediaLoaderActivity() {

    fun setImageViewContent(bitmap: Bitmap) {
        imgSample.setImageBitmap(bitmap)
    }
}
