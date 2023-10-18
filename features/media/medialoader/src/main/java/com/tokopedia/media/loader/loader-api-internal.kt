package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.DEBUG_TIMBER_TAG
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.LoaderRemoteConfig
import com.tokopedia.media.loader.v1.LegacyMediaLoaderApi
import timber.log.Timber

/**
 * A Hansel-able of media-loader improvement (v2).
 *
 * This method it should be able to switch true or false to mitigate unexpected issue for
 * a media-loader improvement. The media-loader has been re-write entirely to improve
 * the image loader performance and APIs, hence the [isImageLoaderV2] method will helps
 * to preventing a particular issue for whole android app ecosystem (both MA and SA).
 *
 * This method is marked as an internal function as we want to ensure the method is
 * only visible within the media-loader module.
 *
 * If [isImageLoaderV2] is false, it will trigger the [LegacyMediaLoaderApi] from:
 * com.tokopedia.media.loader.v1. Those classes inside v1 is removable when the v2 is
 * safe to use for the next 2 app-cycle releases.
 *
 * @return boolean
 */
internal fun isImageLoaderV2(): Boolean {
    return true
}

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    val toggleNewApi = LoaderRemoteConfig.build(context)

    if (context.isValid()) {
        try {
            if (isImageLoaderV2() && toggleNewApi.isNewApiEnabled()) {
                MediaLoaderApi.loadImage(
                    imageView = this,
                    properties = properties.setSource(source)
                )
            } else {
                LegacyMediaLoaderApi.loadImage(
                    imageView = this,
                    isSecure = properties.isSecure,
                    properties = properties.setSource(source)
                )
            }

            if (GlobalConfig.isAllowDebuggingTools()) {
                Timber.d("$DEBUG_TIMBER_TAG: $properties")
            }
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image to show,
            * render with error drawable unify instead.
            * */
            setImageResource(ERROR_RES_UNIFY)
        }
    }
}

/**
 * The [clearImage] extension will help you to stop the image pending specifically on [ImageView] gracefully.
 * You could consider using it to make your page safe to close without any unnecessary bitmap pool that doesn't need to care of.
 *
 * Please make sure to call this extension right on the onDestroy state to avoid a broken image.
 *
 * <b>Sample Usage</b>
 *
 * ```
 * override fun onDestroyState() {
 *    imageView.clearImage()
 * }
 * ```
 */
fun ImageView?.clearImage() {
    if (this != null && context.isValid()) {
        GlideApp.with(this.context).clear(this)
    }
}

internal fun Context?.isValid(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
