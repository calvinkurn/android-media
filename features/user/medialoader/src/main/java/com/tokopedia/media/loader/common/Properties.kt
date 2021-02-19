package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.tokopedia.media.common.data.CDN_IMAGE_URL
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

open class Properties(
        var data: Any? = null,
        var renderDelay: Long = 0L,
        var thumbnailUrl: String = "",
        var blurHash: Boolean = true,
        var isAnimate: Boolean = false,
        var isCircular: Boolean = false,
        var roundedRadius: Float = 0f,
        var signatureKey: Key? = null,
        var error: Int = R.drawable.media_state_default_error,
        var placeHolder: Int = 0,
        var cacheStrategy: MediaCacheStrategy? = MediaCacheStrategy.RESOURCE,
        var overrideSize: Resize? = null,
        var decodeFormat: MediaDecodeFormat? = MediaDecodeFormat.DEFAULT,
        var loaderListener: MediaListener? = null,
        var transform: Transformation<Bitmap>? = null,
        var transforms: List<Transformation<Bitmap>>? = null
) {

    // getting the load time on listener
    internal var loadTime: String = ""

    // validation of performance monitoring
    internal val isTrackable: Boolean = data is String && data.toString().contains(CDN_IMAGE_URL)

    fun setDelay(timeInMs: Long) = apply {
        this.renderDelay = timeInMs
    }

    fun setSource(data: Any?) = apply {
        this.data = data
    }

    fun thumbnailUrl(url: String) = apply {
        this.thumbnailUrl = url
    }

    fun useBlurHash(condition: Boolean) = apply {
        this.blurHash = condition
    }

    fun isAnimate(condition: Boolean) = apply {
        this.isAnimate = condition
    }

    fun isCircular(condition: Boolean) = apply {
        this.isCircular = condition
    }

    fun setRoundedRadius(radius: Float) = apply {
        this.roundedRadius = radius
    }

    fun setSignatureKey(key: Key?) = apply {
        this.signatureKey = key
    }

    fun setErrorDrawable(resourceId: Int) = apply {
        this.error = resourceId
    }

    fun setPlaceHolder(resourceId: Int) = apply {
        this.placeHolder = resourceId
    }

    fun setCacheStrategy(strategy: MediaCacheStrategy) = apply {
        this.cacheStrategy = strategy
    }

    fun overrideSize(newSize: Resize) = apply {
        this.overrideSize = newSize
    }

    fun decodeFormat(format: MediaDecodeFormat) = apply {
        this.decodeFormat = format
    }

    fun listener(
            onSuccess: (Bitmap?, MediaDataSource?) -> Unit = { _, _ -> },
            onError: (MediaException?) -> Unit = { _ -> }
    ) = apply {
        this.loaderListener = object : MediaListener {
            override fun onLoaded(resource: Bitmap?, dataSource: MediaDataSource?) {
                onSuccess(resource, dataSource)
            }

            override fun onFailed(error: MediaException?) {
                onError(error)
            }
        }
    }

    fun transform(transform: Transformation<Bitmap>) = apply {
        this.transform = transform
    }

    fun transforms(transforms: List<Transformation<Bitmap>>) = apply {
        this.transforms = transforms
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Properties &&
                renderDelay == other.renderDelay &&
                loadTime == other.loadTime &&
                isTrackable == other.isTrackable &&
                thumbnailUrl == other.thumbnailUrl &&
                blurHash == other.blurHash &&
                isAnimate == other.isAnimate &&
                isCircular == other.isCircular &&
                roundedRadius == other.roundedRadius &&
                signatureKey == other.signatureKey &&
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
        var result = thumbnailUrl.hashCode()
        result = 31 * result + renderDelay.hashCode()
        result = 31 * result + loadTime.hashCode()
        result = 31 * result + isTrackable.hashCode()
        result = 31 * result + blurHash.hashCode()
        result = 31 * result + isAnimate.hashCode()
        result = 31 * result + isCircular.hashCode()
        result = 31 * result + roundedRadius.hashCode()
        result = 31 * result + signatureKey.hashCode()
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

    override fun toString(): String {
        return "source: $data,\n" +
                "load time: $loadTime,\n" +
                "transform: ${transform?.javaClass?.name},\n" +
                "transforms: ${transforms?.size},\n" +
                "placeholder: $placeHolder,\n" +
                "error: $error,\n" +
                "blurhash: $blurHash,\n" +
                "isAnimate: $isAnimate,\n" +
                "isCircular: $isCircular,\n" +
                "roundedRadius: $roundedRadius,\n"
    }

}