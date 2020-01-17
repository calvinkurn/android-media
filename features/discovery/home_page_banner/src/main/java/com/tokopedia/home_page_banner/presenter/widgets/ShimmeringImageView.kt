package com.tokopedia.home_page_banner.presenter.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.home_page_banner.R
import com.tokopedia.home_page_banner.ext.CrossFadeFactory


class ShimmeringImageView : FrameLayout {
    private var imageView: ImageView? = null
    private var shimmeringView: LoaderImageView? = null

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        val view = View.inflate(context, R.layout.layout_shimmering_image_view, this)
        imageView = view.findViewById(R.id.imageView)
        shimmeringView = view.findViewById(R.id.shimmeringView)
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAllViews()
        imageView = null
        shimmeringView = null

    }
}