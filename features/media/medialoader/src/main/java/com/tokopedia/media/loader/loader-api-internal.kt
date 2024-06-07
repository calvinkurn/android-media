package com.tokopedia.media.loader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.DEBUG_TIMBER_TAG
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.FeatureToggleManager
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import timber.log.Timber

@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValid()) {
        try {
            MediaLoaderApi.loadImage(
                imageView = this,
                properties = properties.setSource(source).also {
                    it.featureToggle = FeatureToggleManager.instance()
                }
            )

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

@Deprecated(
    "Please use MediaBitmapEmptyTarget.clear instead of view extension function",
    replaceWith = ReplaceWith("MediaBitmapEmptyTarget.clear()")
)
fun ImageView?.clearCustomTarget(target: MediaBitmapEmptyTarget<Bitmap>?) {
    if (this != null && context.isValid() && target != null) {
        GlideApp.with(this.context).clear(target)
    }
}

internal fun Context?.isValid(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}
