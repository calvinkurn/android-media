package com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import java.lang.Math.sqrt
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.pow

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

fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = placeholder,
    onSuccess: (() -> Unit)? = null,
    onError: ((e: Exception?) -> Unit)? = null
) {
    val context = this.context
    if (context is Activity && context.isDestroyed) {
        onError?.invoke(IllegalStateException("Passed Activity is Destroyed"))
        return
    }
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onError?.invoke(e)
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onSuccess?.invoke()
                return false
            }
        })
        .into(this)
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

fun Activity.setTransparentSystemBar() {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Long?.isNullOrZero(defaultValue: Long): Long {
    if (this == null || this == 0L) return defaultValue
    return this
}

fun Int?.isNullOrZero(defaultValue: Int): Int {
    if (this == null || this == 0) return defaultValue
    return this
}
fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0F)
    val filter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = filter
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

@Suppress("depe")
object DeviceInfo {
    fun getScreenSizeInInches(context:Context?) : Double{
        if(context!=null){
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getRealMetrics(metrics)
            val widthPx = metrics.widthPixels
            val heightPx = metrics.heightPixels
            val widthInInches = widthPx.toDouble() / metrics.xdpi
            val heightInInches = heightPx.toDouble() / metrics.ydpi
            return sqrt(widthInInches.pow(2.toDouble()) + heightInInches.pow(2.toDouble()))
        }
        return 0.0
    }
}
