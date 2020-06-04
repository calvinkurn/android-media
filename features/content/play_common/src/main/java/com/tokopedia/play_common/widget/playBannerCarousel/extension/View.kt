package com.tokopedia.play_common.widget.playBannerCarousel.extension

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.image.ImageHandler


fun View.showOrHideView(isShow: Boolean){
    this.visibility = if(isShow) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String, listener: ImageHandler.ImageLoaderStateListener? = null){
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    listener?.failedLoad()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    listener?.successLoad()
                    return false
                }
            })
            .into(this)
}

fun View.setGradientBackground(colorArray: List<String>) {
    if (colorArray.size > 1) {
        val colors = IntArray(colorArray.size)
        for (i in colorArray.indices) {
            colors[i] = Color.parseColor(colorArray[i])
        }
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gradient.cornerRadius = 0f
        this.background = gradient
    } else {
        this.setBackgroundColor(Color.parseColor(colorArray[0]))
    }
}