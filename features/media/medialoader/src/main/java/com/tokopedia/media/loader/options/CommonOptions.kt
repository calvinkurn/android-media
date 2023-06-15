@file:SuppressLint("CheckResult")

package com.tokopedia.media.loader.options

import android.annotation.SuppressLint
import com.bumptech.glide.request.BaseRequestOptions
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

internal class CommonOptions constructor(
    private val properties: Properties,
    private val options: BaseRequestOptions<*>
) {

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
            options.circleCrop()
        }
    }

    private fun setErrorDrawable() {
        if (properties.error.isMoreThanZero()) {
            options.error(properties.error)
        }
    }

    private fun isAnimationEnabled() {
        if (properties.isAnimate.not()) {
            options.dontAnimate()
        }
    }

    private fun setCustomSignature() {
        if (properties.isCache) {
            properties.signatureKey?.let {
                options.signature(it)
            }
        } else {
            options.skipMemoryCache(true)
        }
    }

    private fun setCacheStrategy() {
        properties.cacheStrategy?.let {
            options.diskCacheStrategy(MediaCacheStrategy.mapTo(it))
        }
    }

    private fun setDecodeFormat() {
        properties.decodeFormat?.let {
            options.format(MediaDecodeFormat.mapTo(it))
        }
    }

    private fun overrideSize() {
        with(properties) {
            if (overrideSize != null && overrideSize?.width.isMoreThanZero()) {
                val width = overrideSize?.width ?: 0
                val height = overrideSize?.height ?: 0

                options.override(width, height)
            }
        }
    }
}
