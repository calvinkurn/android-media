package com.tokopedia.play.broadcaster.testdouble

import android.net.Uri
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import io.mockk.mockk

/**
 * Created by jegul on 29/09/20
 */
class MockImageTransformer : ImageTransformer {

    override fun transformImageFromUri(uri: Uri): Uri {
        return uri
    }

    override fun parseToUri(id: String): Uri {
        return mockk(relaxed = true)
    }
}
