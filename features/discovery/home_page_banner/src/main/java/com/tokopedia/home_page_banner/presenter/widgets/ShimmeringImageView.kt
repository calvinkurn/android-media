package com.tokopedia.home_page_banner.presenter.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.home_page_banner.R
import com.tokopedia.home_page_banner.ext.CrossFadeFactory
import kotlinx.android.synthetic.main.layout_shimmering_image_view.view.*


class ShimmeringImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr){

    init {
        init()
    }

    private fun init(){
        View.inflate(context, R.layout.layout_shimmering_image_view, this)
        shimmeringView?.visibility = View.GONE
    }

    fun loadImage(url: String){
        shimmeringView?.visibility = View.VISIBLE
        imageView?.let {
            Glide.with(context)
                    .load(url)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.with(CrossFadeFactory()))
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            shimmeringView?.visibility = View.GONE
                            return false
                        }
                    })
                    .into(it)
        }
    }
}