package com.tokopedia.media.loader.data

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.listener.NetworkResponseListener
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

data class Properties(
    internal var data: Any? = null,
    internal var renderDelay: Long = 0L,
    internal var thumbnailUrl: String = "",
    internal var blurHash: Boolean = false,
    internal var isAnimate: Boolean = false,
    internal var isCircular: Boolean = false,
    internal var roundedRadius: Float = 0f,
    internal var signatureKey: Key? = null,
    internal var error: Int = ERROR_RES_UNIFY,
    internal var placeHolder: Int = 0,
    internal var isCache: Boolean = true,
    internal var cacheStrategy: MediaCacheStrategy? = MediaCacheStrategy.RESOURCE,
    internal var overrideSize: Resize? = null,
    internal var decodeFormat: MediaDecodeFormat? = MediaDecodeFormat.DEFAULT,
    internal var loaderListener: MediaListener? = null,
    internal var transform: Transformation<Bitmap>? = null,
    internal var transforms: List<Transformation<Bitmap>>? = null,
    internal var centerCrop: Boolean = false,
    internal var centerInside: Boolean = false,
    internal var fitCenter: Boolean = false,
    internal var isAdaptiveSizeImageRequest: Boolean = false,
    internal var accessToken: String = "",
    internal var userId: String = "",
    internal var shouldTrackNetwork: Boolean = false,
    internal var isForceClearHeaderCache: Boolean = false,
    internal var setNetworkResponse: NetworkResponseListener? = null
) {

    /*
    * get size of imageView to check aspect ratio of image.
    * this size it will be use for blur image size on blurhash generator.
    * */
    internal var imageViewSize: Pair<Int, Int> = Pair(0, 0)

    // getting the load time on listener
    internal var loadTime: String = ""

    // flag to check wether icon or not
    internal var isIcon = false

    // flag to check the image is gif or not
    internal var isGif = false

    // determine is request secure or not
    internal var isSecure = false

    fun isSecure(value: Boolean) = apply {
        isSecure = value
    }

    fun isIcon(value: Boolean) = apply {
        isIcon = value
    }

    fun isGif(value: Boolean) = apply {
        isGif = value
    }

    internal fun setImageSize(width: Int, height: Int) = apply {
        this.imageViewSize = Pair(width, height)
    }

    // flag to able to invoke the [NetworkResponseListener] callback
    fun shouldTrackNetworkResponse(value: Boolean) = apply {
        this.shouldTrackNetwork = value
    }

    // because the header response stored on the disk cache, the page owners have control to
    // set either clear-able cache or not if the cache is not persistent.
    fun setForceClearHeaderCache(value: Boolean) = apply {
        this.isForceClearHeaderCache = value
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

    // set cache validation
    fun useCache(cache: Boolean) = apply {
        this.isCache = cache
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

    // listener to track the header response
    fun networkResponse(
        invoke: (List<Header>) -> Unit = { _ -> },
    ) = apply {
        setNetworkResponse = object : NetworkResponseListener {
            override fun header(data: List<Header>, type: FailureType?) {
                invoke(data)
            }
        }
    }

    // listener to track the header response and failure type
    fun networkResponse(
        invoke: (List<Header>, FailureType?) -> Unit = { _, _ -> },
    ) = apply {
        setNetworkResponse = object : NetworkResponseListener {
            override fun header(data: List<Header>, type: FailureType?) {
                invoke(data, type)
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

    // set built-in centerCrop
    fun centerCrop() = apply {
        this.centerCrop = true
    }

    // set built-in fitCenter
    fun fitCenter() = apply {
        this.fitCenter = true
    }

    // set built-in centerInside
    fun centerInside() = apply {
        this.centerInside = true
    }

    // the adaptive size enabling to set the image size scale based on containers
    fun adaptiveImageSizeRequest(isAdaptive: Boolean) = apply {
        this.isAdaptiveSizeImageRequest = isAdaptive
    }

    // accessToken and userId used to load the secure image using [loadSecureImage]
    fun userSessionAccessToken(token: String) = apply { this.accessToken = token }
    fun userId(userId: String) = apply { this.userId = userId }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Properties &&
            imageViewSize == other.imageViewSize &&
            isIcon == other.isIcon &&
            isSecure == other.isSecure &&
            isGif == other.isGif &&
            renderDelay == other.renderDelay &&
            loadTime == other.loadTime &&
            thumbnailUrl == other.thumbnailUrl &&
            blurHash == other.blurHash &&
            isAnimate == other.isAnimate &&
            isCircular == other.isCircular &&
            roundedRadius == other.roundedRadius &&
            signatureKey == other.signatureKey &&
            error == other.error &&
            placeHolder == other.placeHolder &&
            isCache == other.isCache &&
            cacheStrategy == other.cacheStrategy &&
            overrideSize == other.overrideSize &&
            decodeFormat == other.decodeFormat &&
            loaderListener == other.loaderListener &&
            transform == other.transform &&
            transforms == other.transforms &&
            centerCrop == other.centerCrop &&
            centerInside == other.centerInside &&
            fitCenter == other.fitCenter &&
            accessToken == other.accessToken &&
            userId == other.userId &&
            isAdaptiveSizeImageRequest == other.isAdaptiveSizeImageRequest &&
            shouldTrackNetwork == other.shouldTrackNetwork &&
            isForceClearHeaderCache == other.isForceClearHeaderCache
    }

    override fun hashCode(): Int {
        var result = thumbnailUrl.hashCode()
        result = 3 * result + imageViewSize.hashCode()
        result = 3 * result + isIcon.hashCode()
        result = 3 * result + isGif.hashCode()
        result = 3 * result + isSecure.hashCode()
        result = 3 * result + renderDelay.hashCode()
        result = 3 * result + loadTime.hashCode()
        result = 3 * result + blurHash.hashCode()
        result = 3 * result + isAnimate.hashCode()
        result = 3 * result + isCircular.hashCode()
        result = 3 * result + roundedRadius.hashCode()
        result = 3 * result + signatureKey.hashCode()
        result = 3 * result + error.hashCode()
        result = 3 * result + placeHolder.hashCode()
        result = 3 * result + isCache.hashCode()
        result = 3 * result + cacheStrategy.hashCode()
        result = 3 * result + overrideSize.hashCode()
        result = 3 * result + decodeFormat.hashCode()
        result = 3 * result + loaderListener.hashCode()
        result = 3 * result + transform.hashCode()
        result = 3 * result + transforms.hashCode()
        result = 3 * result + centerCrop.hashCode()
        result = 3 * result + fitCenter.hashCode()
        result = 3 * result + centerInside.hashCode()
        result = 3 * result + accessToken.hashCode()
        result = 3 * result + userId.hashCode()
        result = 3 * result + isAdaptiveSizeImageRequest.hashCode()
        result = 3 * result + shouldTrackNetwork.hashCode()
        result = 3 * result + isForceClearHeaderCache.hashCode()
        return result
    }

}
