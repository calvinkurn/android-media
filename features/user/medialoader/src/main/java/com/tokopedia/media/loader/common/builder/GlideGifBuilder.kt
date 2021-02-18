package com.tokopedia.media.loader.common.builder

import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.common.GlideBuilder
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideRequest

class GlideGifBuilder : GlideBuilder<GifDrawable>() {

    fun build(
            properties: Properties,
            request: GlideRequest<GifDrawable>
    ) = setup(properties, request)

}