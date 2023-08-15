package com.tokopedia.scp_rewards.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(
    context: CoroutineContext = coroutineContext,
    block: suspend CoroutineScope.() -> Unit,
    onError: suspend (Throwable) -> Unit
) =
    launch(context) {
        try {
            block()
        } catch (t: Throwable) {
            if (t is CancellationException) {
                throw t
            } else {
                try {
                    onError(t)
                } catch (e: Throwable) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

suspend fun Context.downloadImage(url: String?) =
    suspendCancellableCoroutine<Bitmap?> { cont ->
        Glide.with(this)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                    cont.cancel()
                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    cont.resumeWith(Result.success(resource))
                    return true
                }
            }).submit()
    }

fun Long?.isNullOrZero(defaultValue: Long): Long {
    if (this == null || this == 0L) return defaultValue
    return this
}

fun Int?.isNullOrZero(defaultValue: Int): Int {
    if (this == null || this == 0) return defaultValue
    return this
}

fun View.show() {
    if (visibility == View.GONE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}

fun launchWeblink(context: Context, webLink: String) {
    RouteManager.route(context, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, Uri.encode(webLink)))
}

fun Context.launchLink(appLink: String? = null, webLink: String? = null) {
    if (TextUtils.isEmpty(appLink)) {
        launchWeblink(this, webLink.orEmpty())
    } else {
        RouteManager.route(this, appLink)
    }
}
