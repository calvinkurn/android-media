package com.tokopedia.play.broadcaster.testdouble

import android.graphics.Bitmap
import android.net.Uri
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil

/**
 * Created by jegul on 29/09/20
 */
class MockPlayCoverImageUtil : PlayCoverImageUtil {

    override fun getImagePathFromBitmap(image: Bitmap): Uri {
        return Uri.EMPTY
    }
}