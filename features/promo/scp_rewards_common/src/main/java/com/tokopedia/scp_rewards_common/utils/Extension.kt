package com.tokopedia.scp_rewards_common.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale

fun LottieAnimationView.loadLottieFromUrl(
    url: String?,
    onLottieLoaded: (composition: LottieComposition) -> Unit = { },
    onLottieEnded: () -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    animationUpdateListener: (ValueAnimator?) -> Unit = {},
    autoPlay: Boolean = false
) {
    url?.let {
        setFailureListener(onError)
        addLottieOnCompositionLoadedListener(onLottieLoaded)
        addAnimationEndListener(onLottieEnded)
        setAnimationFromUrl(url)
        addAnimatorUpdateListener(animationUpdateListener)

        if (autoPlay) {
            playAnimation()
        }
    } ?: run { onError }
}

fun LottieAnimationView.addAnimationEndListener(action: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            action()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationStart(animation: Animator) {}
    })
}

fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0F)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
}

fun ImageView.grayScaleFilter() {
    val colorMatrix = ColorMatrix()
    colorMatrix.set(
        floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    colorFilter = ColorMatrixColorFilter(colorMatrix)
}

fun Typography.showTextOrHide(text: String?) {
    if (text.isNullOrEmpty()) {
        this.hide()
    } else {
        this.text = text
        visible()
    }
}

fun ImageUnify.loadImageOrFallback(imageUrl: String?, fallback: Int = 0, onFallback: () -> Unit = {}) {
    imageUrl?.let { safeUrl ->
        this.setImageUrl(safeUrl)
        this.onUrlLoaded = { isSuccess ->
            if (isSuccess.not() && fallback != 0) {
                onFallback()
                this.post {
                    this.setImageDrawable(
                        ContextCompat.getDrawable(this.context, fallback)
                    )
                }
            }
        }
    } ?: run {
        onFallback()
        if (fallback != 0) {
            this.setImageDrawable(ContextCompat.getDrawable(this.context, fallback))
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

