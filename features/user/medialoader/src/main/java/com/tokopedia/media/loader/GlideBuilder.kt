package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.utils.BLUR_HASH_QUERY
import com.tokopedia.media.loader.utils.mediaSignature
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat

object GlideBuilder {

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

    @JvmOverloads
    fun loadImage(data: Any?, imageView: ImageView, properties: Properties) {
        with(properties) {
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

                    Loader.glideUrl(source).also { glideUrl ->
                        source = glideUrl
                        signatureKey = signatureKey.mediaSignature(glideUrl)
                    }
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
                        if (!isCircular || !isFreeOngkirIcon(source)) {
                            blurHash(source) { hash ->
                                placeholder(BitmapDrawable(context.resources, blurring(hash)))
                            }
                        } else {
                            placeholder(R.drawable.ic_media_default_placeholder)
                        }
                    }

                    if (thumbnailUrl.isNotEmpty()) thumbnail(thumbnailLoader(context, thumbnailUrl))
                    if (roundedRadius != 0f) transform(RoundedCorners(roundedRadius.toInt()))
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

                    listener(glideListener(loaderListener))

                }.into(imageView)
            }
        }
    }

    private fun isFreeOngkirIcon(source: Any?): Boolean {
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
                width = 20,
                height = 12
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Bitmap> {
        return GlideApp.with(context)
                .asBitmap()
                .load(resource)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
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

}