package com.tokopedia.scp_rewards_touchpoints.bottomsheet.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy

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
    this.loadImage(url) {
        setCacheStrategy(MediaCacheStrategy.ALL)
        listener(
            onSuccess = { _, _ ->
                onSuccess?.invoke()
            },
            onError = {
                onError?.invoke(it)
            }
        )
    }
}

@SuppressLint("DeprecatedMethod")
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
