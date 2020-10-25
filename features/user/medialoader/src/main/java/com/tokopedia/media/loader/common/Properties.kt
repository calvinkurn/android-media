package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.target.Target
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

open class Properties(
        var target: Target? = null,
        var data: Any? = null,
        var thumbnailUrl: String = "",
        var isAnimate: Boolean = false,
        var isCircular: Boolean = false,
        var roundedRadius: Float = 0f,
        var signature: Key? = null,
        var error: Int = R.drawable.ic_media_default_error,
        var placeHolder: Int = 0, // R.drawable.ic_media_default_placeholder
        var cacheStrategy: MediaCacheStrategy? = MediaCacheStrategy.ALL,
        var overrideSize: Resize? = null,
        var decodeFormat: MediaDecodeFormat? = MediaDecodeFormat.DEFAULT,
        var loaderListener: LoaderStateListener? = null,
        var transform: Transformation<Bitmap>? = null,
        var transforms: List<Transformation<Bitmap>>? = null
) {

    val size = overrideSize
    val decode = decodeFormat
    val singleTransform = transform
    val customSignature = signature

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Properties &&
                target == other.target &&
                data == other.data &&
                thumbnailUrl == other.thumbnailUrl &&
                isAnimate == other.isAnimate &&
                isCircular == other.isCircular &&
                roundedRadius == other.roundedRadius &&
                signature == other.signature &&
                error == other.error &&
                placeHolder == other.placeHolder &&
                cacheStrategy == other.cacheStrategy &&
                overrideSize == other.overrideSize &&
                decodeFormat == other.decodeFormat &&
                loaderListener == other.loaderListener &&
                transform == other.transform &&
                transforms == other.transforms
    }

    override fun hashCode(): Int {
        var result = target.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        result = 31 * result + isAnimate.hashCode()
        result = 31 * result + isCircular.hashCode()
        result = 31 * result + roundedRadius.hashCode()
        result = 31 * result + signature.hashCode()
        result = 31 * result + error.hashCode()
        result = 31 * result + placeHolder.hashCode()
        result = 31 * result + cacheStrategy.hashCode()
        result = 31 * result + overrideSize.hashCode()
        result = 31 * result + decodeFormat.hashCode()
        result = 31 * result + loaderListener.hashCode()
        result = 31 * result + transform.hashCode()
        result = 31 * result + transforms.hashCode()
        return result
    }

}