package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.utils.MediaTarget

fun <T: View> loadImageWithTarget(
    context: Context,
    url: String,
    properties: Properties.() -> Unit,
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

fun loadImageWithEmptyTarget(
    context: Context,
    url: String,
    properties: Properties.() -> Unit,
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