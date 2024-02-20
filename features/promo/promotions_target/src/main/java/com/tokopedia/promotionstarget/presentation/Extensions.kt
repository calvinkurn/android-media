package com.tokopedia.promotionstarget.presentation

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun AppCompatImageView.loadImageGlide(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        url?.getBitmapImageUrl(context, {
            isAnimate(false)
        }, MediaBitmapEmptyTarget(
            onReady = {
                onLoadingFinished(true)
            },
            onFailed = {
                onLoadingFinished(false)
            },
            onCleared = {
                onLoadingFinished(false)
            }
        ))
    }
}

fun AppCompatImageView.loadImageWithNoPlaceholder(url: String?, onLoadingFinished: (success: Boolean) -> Unit = {}) {
    if (!TextUtils.isEmpty(url)) {
        url?.getBitmapImageUrl(context, {
           isAnimate(false)
        }, MediaBitmapEmptyTarget(
            onReady = {
                onLoadingFinished(true)
            },
            onFailed = {
                onLoadingFinished(false)
            }
        ))
    }
}


fun AppCompatImageView.loadImageGlide(id: Int) {
    this.loadImage(id)
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
