package com.tokopedia.media.loader.module

import android.content.Context
import com.bumptech.glide.load.model.*
import java.io.InputStream

class AdaptiveImageSizeFactory constructor(
    private val context: Context
) : ModelLoaderFactory<String, InputStream> {

    private val modelCache = ModelCache<String, GlideUrl?>(500)

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
        val loader = multiFactory.build(
            GlideUrl::class.java,
            InputStream::class.java
        )

        return AdaptiveImageSizeLoader(context, loader, modelCache)
    }

    override fun teardown() {}
}
