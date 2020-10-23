package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.utils.AttributeUtils
import com.tokopedia.media.loader.utils.BLUR_HASH_QUERY
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat

object GlideBuilder {

    private const val MEDIA_LOADER_TRACE = "mp_medialoader"
    private const val URL_PREFIX = "https://ecs7-p.tokopedia.net/img/cache/"

    private fun glideListener(
            listener: LoaderStateListener?,
            performanceMonitoring: PerformanceMonitoring?
    ) = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.failedLoad(e)
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            performanceMonitoring?.stopTrace()
            listener?.successLoad(resource, mapToDataSource(dataSource))
            return false
        }
    }

    @JvmOverloads
    fun loadImage(
            imageView: ImageView,
            thumbnailUrl: String,
            url: Any?,
            radius: Float = 0f,
            signatureKey: Key?,
            cacheStrategy: MediaCacheStrategy?,
            @DrawableRes placeHolder: Int = 0,
            @DrawableRes resOnError: Int,
            isAnimate: Boolean = false,
            isCircular: Boolean = false,
            overrideSize: Resize? = null,
            decodeFormat: MediaDecodeFormat? = null,
            stateListener: LoaderStateListener? = null,
            transform: Transformation<Bitmap>? = null,
            transforms: List<Transformation<Bitmap>>? = null
    ) {
        val localTransform = mutableListOf<Transformation<Bitmap>>()

        val drawableError = if (resOnError != 0) {
            getDrawable(imageView.context, resOnError)
        } else {
            getDrawable(imageView.context, R.drawable.ic_media_default_error)
        }

        if (url == null) {
            imageView.setImageDrawable(drawableError)
        } else {
            var performanceMonitoring: PerformanceMonitoring? = null
            if(url is GlideUrl) {
                performanceMonitoring = getPerformanceMonitoring(url.toStringUrl(), imageView.context)
            }

            GlideApp.with(imageView.context).load(url).apply {
                if (thumbnailUrl.isNotEmpty()) {
                    thumbnail(thumbnailLoader(imageView.context, thumbnailUrl))
                } else {
                    if (url is String) {
                        url.toUri()?.let {
                            if (it.getQueryParameters(BLUR_HASH_QUERY).isNotEmpty()) {
                                val blurHash = it.getQueryParameter(BLUR_HASH_QUERY)
                                thumbnail(thumbnailLoader(imageView.context, blurring(imageView, blurHash)))
                            }
                        }
                    }
                }

                when (imageView.scaleType) {
                    ImageView.ScaleType.FIT_CENTER -> fitCenter()
                    ImageView.ScaleType.CENTER_CROP -> centerCrop()
                    ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                    else -> {}
                }

                if (overrideSize != null) override(overrideSize.width, overrideSize.height)
                if (decodeFormat != null) format(mapToDecodeFormat(decodeFormat))
                if (radius != 0f) transform(RoundedCorners(radius.toInt()))
                if (transform != null) localTransform.add(transform)
                if (signatureKey != null) signature(signatureKey)
                if (isCircular) localTransform.add(CircleCrop())
                if (!isAnimate) dontAnimate()

                drawableError?.let { drawable -> error(drawable) }
                cacheStrategy?.let { diskCacheStrategy(mapToDiskCacheStrategy(it)) }
                transforms?.let { localTransform.addAll(it) }

                if (localTransform.isNotEmpty()) {
                    transform(MultiTransformation(localTransform))
                }

                listener(glideListener(stateListener, performanceMonitoring))
                into(imageView)
            }
        }
    }

    fun blurring(imageView: ImageView, blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = imageView.maxWidth,
                height = imageView.maxHeight
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Drawable> {
        return GlideApp.with(context).load(resource).fitCenter().diskCacheStrategy(DiskCacheStrategy.DATA)
    }

    fun loadGifImage(imageView: ImageView, url: String) {
        with(imageView) {
            GlideApp.with(context)
                    .asGif()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(RoundedCorners(10))
                    .into(this)
        }
    }

    private fun getPerformanceMonitoring(url: String, context: Context): PerformanceMonitoring {
        val urlWithoutPrefix = url.removePrefix(URL_PREFIX)
        val mediaSetting = MediaSettingPreferences(context)
        val mediaSettingIndex = mediaSetting.qualitySettings()

        val performanceMonitoring = PerformanceMonitoring.start(MEDIA_LOADER_TRACE)
        performanceMonitoring?.putCustomAttribute("image_url", urlWithoutPrefix)
        performanceMonitoring?.putCustomAttribute("image_quality_setting", mediaSetting.getQualitySetting(mediaSettingIndex))
        performanceMonitoring?.putCustomAttribute("date_time", AttributeUtils.getDateTime())

        return performanceMonitoring
    }

}