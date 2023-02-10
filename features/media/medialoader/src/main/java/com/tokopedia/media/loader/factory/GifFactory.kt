package com.tokopedia.media.loader.factory

import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.module.GlideRequest

class GifFactory : MediaLoaderFactory<GifDrawable>() {

    fun build(
        properties: Properties,
        request: GlideRequest<GifDrawable>
    ) = setup(properties, request)

}
