package com.tokopedia.promotionstarget

import android.support.v7.widget.AppCompatImageView
import android.util.DisplayMetrics
import android.view.View
import com.bumptech.glide.Glide

fun AppCompatImageView.loadImageGlide(url:String){
    Glide.with(context)
            .load(url)
            .into(this)
}

fun View.dpToPx(dp:Int):Float{
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}