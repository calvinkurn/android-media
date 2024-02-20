package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

fun String.getBitmapFromUrl(
    context: Context,
    timeout: Long = DEFAULT_TIMEOUT_MS,
    properties: Properties.() -> Unit = {}
): Bitmap? {
    return MediaLoaderTarget.loadImageFuture(context, timeout, Properties().apply(properties).setSource(this))
}

fun Uri.getBitmapFromUrl(
    context: Context,
    timeout: Long = DEFAULT_TIMEOUT_MS,
    properties: Properties.() -> Unit = {}
): Bitmap? {
    return MediaLoaderTarget.loadImageFuture(context, timeout, Properties().apply(properties).setSource(this))
}

fun String.getBitmapImageUrlAsFlow(
    context: Context,
    properties: Properties.() -> Unit = {}
): Flow<Bitmap> {
    val url = this
    return callbackFlow {
        MediaLoaderTarget.loadImage(
            context,
            Properties()
                .apply(properties)
                .setSource(url),
            MediaBitmapEmptyTarget(
                onReady = {
                    trySend(it)
                }
            )
        )

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

fun MediaBitmapEmptyTarget<Bitmap>.clear(context: Context) {
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
