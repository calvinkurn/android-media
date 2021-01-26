package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.BitmapCompat
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
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
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.utils.AttributeUtils
import com.tokopedia.media.loader.utils.BLUR_HASH_QUERY
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat

object GlideBuilder {

    private const val MEDIA_LOADER_TRACE = "mp_medialoader"
    private const val URL_PREFIX = "https://ecs7-p.tokopedia.net/img/cache/"

    private val blurHashRandom = listOf(
            "A4ADcRuO_2y?",
            "A9K{0B#R3WyY",
            "AHHUnD~V^ia~",
            "A2N+X[~qv]IU",
            "ABP?2U~X5J^~"
    )

    private val exceptionBlurring = listOf(
            "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
    )

    private fun ImageView.resourceError(errorRes: Int) =
            getDrawable(context, if (errorRes != 0) {
                errorRes
            } else {
                R.drawable.ic_media_default_error
            })

    private fun glideListener(
            startTime: Long,
            listener: LoaderStateListener?,
            performanceMonitoring: PerformanceMonitoring?
    ) = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.failedLoad(e)
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            val fileSize = resource?.let { BitmapCompat.getAllocationByteCount(it) }?: 0
            val endTime = System.currentTimeMillis()

            performanceMonitoring?.putCustomAttribute("load_time", (endTime - startTime).toString())
            performanceMonitoring?.putCustomAttribute("file_size", fileSize.toString())

            performanceMonitoring?.stopTrace()
            listener?.successLoad(resource, mapToDataSource(dataSource))
            return false
        }
    }

    @JvmOverloads
    fun loadImage(data: Any?, imageView: ImageView, properties: Properties) {
        with(properties) {
            var performanceMonitoring: PerformanceMonitoring? = null
            val startTime = System.currentTimeMillis()

            val localTransform = mutableListOf<Transformation<Bitmap>>()
            val drawableError = imageView.resourceError(error)
            val context = imageView.context
            var source = data

            if (source == null) {
                imageView.setImageDrawable(drawableError)
            } else {
                if (source is String) {
                    if (source.isEmpty()) {
                        imageView.loadImage(R.drawable.ic_media_default_error)
                        return
                    }

                    source = Loader.glideUrl(source)
                    performanceMonitoring = getPerformanceMonitoring(source, context)
                }

                GlideApp.with(context).asBitmap().load(source).apply {
                    when (imageView.scaleType) {
                        ImageView.ScaleType.FIT_CENTER -> fitCenter()
                        ImageView.ScaleType.CENTER_CROP -> centerCrop()
                        ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                        else -> {}
                    }

                    if (placeHolder != 0) {
                        placeholder(placeHolder)
                    } else {
                        if (!isCircular || !imageExcludeList(source)) {
                            blurHash(source) { hash ->
                                val bitmapHash = BitmapDrawable(context.resources, blurring(hash))
                                thumbnail(thumbnailLoader(context, bitmapHash))
                                placeholder(bitmapHash)
                            }
                        } else {
                            placeholder(R.drawable.ic_media_default_placeholder)
                        }
                    }

                    if (thumbnailUrl.isNotEmpty()) thumbnail(thumbnailLoader(context, thumbnailUrl))
                    if (roundedRadius != 0f) localTransform.add(RoundedCorners(roundedRadius.toInt()))
                    if (isCircular) localTransform.add(CircleCrop())
                    if (!isAnimate) dontAnimate()

                    cacheStrategy?.let { diskCacheStrategy(mapToDiskCacheStrategy(it)) }
                    overrideSize?.let { override(it.width, it.height) }
                    decodeFormat?.let { format(mapToDecodeFormat(it)) }
                    transforms?.let { localTransform.addAll(it) }
                    transform?.let { localTransform.add(it) }
                    signatureKey?.let { signature(it) }
                    drawableError?.let { error(it) }

                    if (localTransform.isNotEmpty()) {
                        transform(MultiTransformation(localTransform))
                    }

                    listener(glideListener(startTime, loaderListener, performanceMonitoring))

                }.into(imageView)
            }
        }
    }

    private fun imageExcludeList(source: Any?): Boolean {
        return if (source is GlideUrl) exceptionBlurring.contains(source.toStringUrl()) else false
    }

    private fun blurHash(url: Any?, blurHash: (String?) -> Unit) {
        if (url is GlideUrl) {
            val hash = url.toStringUrl().toUri()?.getQueryParameter(BLUR_HASH_QUERY)
            if (!hash.isNullOrEmpty()) {
                blurHash(hash)
            } else {
                blurHash(blurHashRandom.random())
            }
        }
    }

    private fun blurring(blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = 60,
                height = 20
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

    fun loadGifImage(imageView: ImageView, data: String) {
        with(imageView) {
            GlideApp.with(context)
                    .asGif()
                    .load(data)
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