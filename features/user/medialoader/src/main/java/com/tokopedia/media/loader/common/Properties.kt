package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.tokopedia.media.common.data.CDN_IMAGE_URL
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.utils.AspectRatio
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

open class Properties(
        internal var data: Any? = null,
        internal var renderDelay: Long = 0L,
        internal var thumbnailUrl: String = "",
        internal var blurHash: Boolean = true,
        internal var isAnimate: Boolean = false,
        internal var isCircular: Boolean = false,
        internal var roundedRadius: Float = 0f,
        internal var signatureKey: Key? = null,
        internal var error: Int = R.drawable.media_state_default_error,
        internal var placeHolder: Int = 0,
        internal var cacheStrategy: MediaCacheStrategy? = MediaCacheStrategy.RESOURCE,
        internal var overrideSize: Resize? = null,
        internal var decodeFormat: MediaDecodeFormat? = MediaDecodeFormat.DEFAULT,
        internal var loaderListener: MediaListener? = null,
        internal var transform: Transformation<Bitmap>? = null,
        internal var transforms: List<Transformation<Bitmap>>? = null
) {

    /*
    * get size of imageView to check aspect ratio of image.
    * this size it will be use for blur image size on blurhash generator.
    * */
    internal var imageViewSize: Pair<Int, Int> = Pair(0, 0)

    // generated URL have contains ECT param
    internal var urlHasQualityParam: String = ""

    // getting the load time on listener
    internal var loadTime: String = ""

    // validation of performance monitoring
    internal val isTrackable: Boolean = data is String && data.toString().contains(CDN_IMAGE_URL)

    internal fun setImageSize(width: Int, height: Int) = apply {
        this.imageViewSize = Pair(width, height)
    }

    internal fun setUrlHasQuality(url: String) = apply {
        this.urlHasQualityParam = url
    }

    // to display the image with specific time to delay (ms)
    fun setDelay(timeInMs: Long) = apply {
        this.renderDelay = timeInMs
    }

    // set the main URL of image
    fun setSource(data: Any?) = apply {
        this.data = data
    }

    // display a thumbnail before rendering the actual image
    fun thumbnailUrl(url: String) = apply {
        this.thumbnailUrl = url
    }

    // an activation of blurHash (as the placeholder replacement)
    fun useBlurHash(condition: Boolean) = apply {
        this.blurHash = condition
    }

    // display image with built-in Glide animation
    fun isAnimate(condition: Boolean) = apply {
        this.isAnimate = condition
    }

    // transform the actual image into circleCrop()
    fun isCircular(condition: Boolean) = apply {
        this.isCircular = condition
    }

    // transform the actual image into RoundedRadius() with specific radius for the corners
    fun setRoundedRadius(radius: Float) = apply {
        this.roundedRadius = radius
    }

    // use custom signature key for image caching
    fun setSignatureKey(key: Key?) = apply {
        this.signatureKey = key
    }

    // set custom error drawable
    fun setErrorDrawable(resourceId: Int) = apply {
        this.error = resourceId
    }

    // set custom placeholder drawable
    fun setPlaceHolder(resourceId: Int) = apply {
        this.placeHolder = resourceId
    }

    // use custom cache strategy for image rendering
    fun setCacheStrategy(strategy: MediaCacheStrategy) = apply {
        this.cacheStrategy = strategy
    }

    // overriding the size of actual image before display
    fun overrideSize(newSize: Resize) = apply {
        this.overrideSize = newSize
    }

    // use custom decode format, default: PREFER_ARGB_8888/DEFAULT
    fun decodeFormat(format: MediaDecodeFormat) = apply {
        this.decodeFormat = format
    }

    // use custom listener for the image loader callback
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

    // mapping single transform
    fun transform(transform: Transformation<Bitmap>) = apply {
        this.transform = transform
    }

    // mapping multiple transform
    fun transforms(transforms: List<Transformation<Bitmap>>) = apply {
        this.transforms = transforms
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Properties &&
                isTrackable == other.isTrackable &&
                imageViewSize == other.imageViewSize &&
                urlHasQualityParam == other.urlHasQualityParam &&
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
        result = 31 * result + isTrackable.hashCode()
        result = 31 * result + imageViewSize.hashCode()
        result = 31 * result + urlHasQualityParam.hashCode()
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
        val ratio = AspectRatio.calculate(imageViewSize.first, imageViewSize.second)
        return "source: $data,\n" +
                "is trackable: $isTrackable,\n" +
                "url has ect: $urlHasQualityParam,\n" +
                "imageview size: ${imageViewSize.first} * ${imageViewSize.second},\n" +
                "imageview ratio: ${ratio.first} * ${ratio.second},\n" +
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