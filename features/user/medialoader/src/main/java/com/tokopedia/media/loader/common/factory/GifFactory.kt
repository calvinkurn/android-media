package com.tokopedia.media.loader.common.factory

import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.common.MediaLoaderFactory
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideRequest

class GifFactory : MediaLoaderFactory<GifDrawable>() {

    fun build(
            properties: Properties,
            request: GlideRequest<GifDrawable>
    ) = setup(properties, request)

}