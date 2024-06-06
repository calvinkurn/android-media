package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.tokopedia.media.loader.data.BitmapFlowResult
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.AppWidgetTarget
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

private const val DEFAULT_TIMEOUT_MS = 2_500L // 2.5 sec

fun String.getBitmapImageUrl(
    context: Context,
    properties: Properties.() -> Unit = {},
    target: MediaBitmapEmptyTarget<Bitmap> = MediaBitmapEmptyTarget()
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        target
    )
}

fun Uri.getBitmapImageUrl(
    context: Context,
    properties: Properties.() -> Unit = {},
    target: MediaBitmapEmptyTarget<Bitmap> = MediaBitmapEmptyTarget()
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        target
    )
}

fun Uri.getGifDrawable(
    context: Context,
    properties: Properties.() -> Unit = {},
    target: MediaBitmapEmptyTarget<GifDrawable> = MediaBitmapEmptyTarget()
) {
    MediaLoaderTarget.loadGif(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        target
    )
}

fun String.getBitmapImageUrl(
    context: Context,
    properties: Properties.() -> Unit = {},
    onReady: (Bitmap) -> Unit
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        MediaBitmapEmptyTarget(
            onReady = {
                onReady(it)
            }
        )
    )
}

fun Any.getBitmapFromUrl(
    context: Context,
    timeout: Long = DEFAULT_TIMEOUT_MS,
    properties: Properties.() -> Unit = {}
): Bitmap? {
    return MediaLoaderTarget.loadImageFuture(context, timeout, Properties().apply(properties).setSource(this))
}

fun String.downloadImageFromUrl(
    context: Context,
    properties: Properties.() -> Unit = {}
): File? {
    return MediaLoaderTarget.downloadImageFuture(context, Properties().apply(properties).setSource(this))
}

fun AppWidgetTarget.loadImage(context: Context, url: String, properties: Properties.() -> Unit) {
    MediaLoaderTarget.loadImageAWT(context, this,  Properties().apply(properties).setSource(url))
}

fun AppWidgetTarget.loadImage(context: Context, resId: Int, properties: Properties.() -> Unit) {
    MediaLoaderTarget.loadImageAWT(context, this,  Properties().apply(properties).setSource(resId))
}

fun Any.preloadImage(
    context: Context
) {
    MediaLoaderTarget.imagePreload(context, this)
}

fun String.getBitmapImageUrlAsFlow(
    context: Context,
    properties: Properties.() -> Unit = {}
): Flow<BitmapFlowResult> {
    val url = this
    return callbackFlow {
        Properties().apply(properties).setSource(url).let {
            it.listener(
                onError = { e ->
                    trySend(BitmapFlowResult.Failed(e))
                }
            )

            MediaLoaderTarget.loadImage(
                context,
                it,
                MediaBitmapEmptyTarget(
                    onReady = { bitmapResult ->
                        trySend(BitmapFlowResult.Success(bitmapResult))
                    }
                )
            )
        }

        awaitClose { channel.close() }
    }
}

fun View.loadImageBackground(
    url: String,
    properties: Properties.() -> Unit = {}
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(url),
        MediaBitmapEmptyTarget(
            onReady = {
                this.background = it.toDrawable(this.resources)
            }
        )
    )
}

fun Int.loadResource(
    context: Context,
    properties: Properties.() -> Unit = {},
    target: MediaBitmapEmptyTarget<Bitmap> = MediaBitmapEmptyTarget()
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        target
    )
}

fun Bitmap.loadResource(
    context: Context,
    properties: Properties.() -> Unit = {},
    target: MediaBitmapEmptyTarget<Bitmap> = MediaBitmapEmptyTarget()
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(this),
        target
    )
}

fun <T : Any> MediaBitmapEmptyTarget<T>.clear(context: Context) {
    MediaLoaderTarget.clear(context, this)
}

@Deprecated(
    message = "This function is too verbose, please use getBitmapImageUrl() extension instead",
    replaceWith = ReplaceWith("getBitmapImageUrl")
)
fun <T : View> loadImageWithTarget(
    context: Context,
    url: String,
    properties: Properties.() -> Unit = {},
    mediaTarget: MediaTarget<T>
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(url),
        mediaTarget
    )
}

@Deprecated(
    message = "This function is too verbose, please use getBitmapImageUrl() extension instead",
    replaceWith = ReplaceWith("imageUrl.getBitmapImageUrl(context, properties)")
)
fun loadImageWithEmptyTarget(
    context: Context,
    url: String,
    properties: Properties.() -> Unit = {},
    mediaTarget: MediaBitmapEmptyTarget<Bitmap>
) {
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(url),
        mediaTarget
    )
}
