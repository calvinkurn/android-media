package com.tokopedia.promotionstarget.presentation

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.promotionstarget.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun AppCompatImageView.loadImageGlide(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.t_promo_placeholder)
                .dontAnimate()
                .dontTransform()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        onLoadingFinished(false)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        onLoadingFinished(false)
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        this@loadImageGlide.setImageBitmap(resource)
                        onLoadingFinished(true)
                    }
                })
    }
}

fun AppCompatImageView.loadImageWithNoPlaceholder(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        onLoadingFinished(true)
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        onLoadingFinished(false)
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

fun AppCompatImageView.dim() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)
    colorFilter = ColorMatrixColorFilter(matrix)
}

fun AppCompatImageView.unDim() {
    val matrix = ColorMatrix()
    matrix.setSaturation(1f)
    colorFilter = ColorMatrixColorFilter(matrix)
}