package com.tokopedia.promotionstarget

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun AppCompatImageView.loadImageGlide(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(context)
                .load(url)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        onLoadingFinished(false)
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        onLoadingFinished(true)
                        return false
                    }

                })
                .into(this)
    }
}

fun AppCompatImageView.loadImageGlide(id: Int) {
    Glide.with(context)
            .load(id)
            .into(this)
}

fun View.dpToPx(dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend (() -> Unit),
                                    onError: suspend (Throwable) -> Unit) =
        launch(context) {
            try {
                block()
            } catch (t: Throwable) {
                try {
                    onError(t)
                } catch (e: Throwable) {

                }
            }
        }

fun getColor(context: Context, id: Int): Int {
    try {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, id)
        } else {
            context.resources.getColor(id)
        }
    } catch (e: NullPointerException) {
        e.printStackTrace()
        return 0
    }
}

