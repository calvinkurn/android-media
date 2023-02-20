package com.tokopedia.media.loader.options

import com.bumptech.glide.request.RequestOptions
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

class CommonOptions(private val properties: Properties) : RequestOptions() {

    init {
        setCircleCrop()
        setErrorDrawable()
        isAnimationEnabled()
        setCustomSignature()
        setCacheStrategy()
        setDecodeFormat()
        overrideSize()
    }

    private fun setCircleCrop() {
        if (properties.isCircular) {
            circleCrop()
        }
    }

    private fun setErrorDrawable() {
        error(properties.error)
    }

    private fun isAnimationEnabled() {
        if (properties.isAnimate.not()) {
            dontAnimate()
        }
    }

    private fun setCustomSignature() {
        if (properties.isCache) {
            properties.signatureKey?.let {
                signature(it)
            }
        } else {
            skipMemoryCache(true)
        }
    }

    private fun setCacheStrategy() {
        properties.cacheStrategy?.let {
            diskCacheStrategy(MediaCacheStrategy.mapTo(it))
        }
    }

    private fun setDecodeFormat() {
        properties.decodeFormat?.let {
            format(MediaDecodeFormat.mapTo(it))
        }
    }

    private fun overrideSize() {
        with(properties) {
            if (overrideSize != null && overrideSize?.width.isMoreThanZero()) {
                val width = overrideSize?.width?: 0
                val height = overrideSize?.height?: 0

                override(width, height)
            }
        }
    }

    companion object {
        @Volatile
        private var factory: CommonOptions? = null

        fun build(properties: Properties): RequestOptions {
            return factory ?: CommonOptions(properties).also {
                factory = it
            }
        }
    }
}
