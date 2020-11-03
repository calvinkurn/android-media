package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.Priority
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
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.utils.BLUR_HASH_QUERY
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat

object GlideBuilder {

    private val blurHashRandom = listOf(
            "A4ADcRuO_2y?",
            "A9K{0B#R3WyY",
            "AHHUnD~V^ia~",
            "A2N+X[~qv]IU",
            "ABP?2U~X5J^~"
    )

    private fun glideListener(
            listener: LoaderStateListener?
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
            listener?.successLoad(resource, mapToDataSource(dataSource))
            return false
        }
    }

    private fun glideRequest(context: Context): GlideRequest<Bitmap> {
        return GlideApp
                .with(context)
                .asBitmap()
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
        val context = imageView.context

        val drawableError = if (resOnError != 0) {
            getDrawable(imageView.context, resOnError)
        } else {
            getDrawable(imageView.context, R.drawable.ic_media_default_error)
        }

        if (url == null) {
            imageView.setImageDrawable(drawableError)
        } else {
            glideRequest(context).apply {

                when (imageView.scaleType) {
                    ImageView.ScaleType.FIT_CENTER -> fitCenter()
                    ImageView.ScaleType.CENTER_CROP -> centerCrop()
                    ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                    else -> {}
                }

                if (thumbnailUrl.isNotEmpty()) thumbnail(thumbnailLoader(context, thumbnailUrl))
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

                if (placeHolder != 0) {
                    placeholder(placeHolder)
                } else {
                    if (!isCircular) {
                        blurHashFromUrl(url) { hash ->
                            placeholder(BitmapDrawable(context.resources, blurring(hash)))
                        }
                    } else {
                        placeholder(R.drawable.ic_media_default_placeholder)
                    }
                }

                listener(glideListener(stateListener))

            }.load(url).into(imageView)
        }
    }

    private fun blurHashFromUrl(url: Any?, blurHash: (String?) -> Unit) {
        if (url is GlideUrl) {
            val hash = url.toStringUrl().toUri()?.getQueryParameter(BLUR_HASH_QUERY)
            if (hash != null && hash.isNotEmpty()) {
                blurHash(hash)
            } else {
                blurHash(blurHashRandom.random())
            }
        }
    }

    private fun blurring(blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = 80,
                height = 80,
                useCache = true
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return glideRequest(context)
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
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

}