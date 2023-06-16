package com.tokopedia.play.broadcaster.testdouble

import android.net.Uri
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import io.mockk.mockk

/**
 * Created by jegul on 29/09/20
 */
class MockImageTransformer : ImageTransformer {

    private var transformImageFromUriResponse: (() -> Uri)? = null

    override fun transformImageFromUri(uri: Uri): Uri {
        return transformImageFromUriResponse?.invoke() ?: uri
    }

    override fun parseToUri(id: String): Uri {
        return mockk(relaxed = true)
    }

    fun setTransformImageFromUriResponse(transformImageFromUriResponse: () -> Uri) {
        this.transformImageFromUriResponse = transformImageFromUriResponse
    }
}
