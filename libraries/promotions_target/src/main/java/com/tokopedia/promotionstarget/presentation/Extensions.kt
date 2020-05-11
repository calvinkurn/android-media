package com.tokopedia.promotionstarget.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.promotionstarget.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun AppCompatImageView.loadImageGlide(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.t_promo_placeholder)
                .dontAnimate()
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        onLoadingFinished(false)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        onLoadingFinished(false)
                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        this@loadImageGlide.setImageDrawable(resource)
                        onLoadingFinished(true)
                    }
                })
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
        return 0
    }
}

