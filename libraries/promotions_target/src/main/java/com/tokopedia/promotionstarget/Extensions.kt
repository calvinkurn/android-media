package com.tokopedia.promotionstarget

import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import com.bumptech.glide.Glide

fun AppCompatImageView.loadImageGlide(url: String?) {
    if (!TextUtils.isEmpty(url)) {
        Glide.with(context)
                .load(url)
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