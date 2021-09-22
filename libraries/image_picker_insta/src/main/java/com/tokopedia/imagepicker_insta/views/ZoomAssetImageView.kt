package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.otaliastudios.zoom.ZoomEngine
import com.otaliastudios.zoom.ZoomImageView
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import timber.log.Timber

class ZoomAssetImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ZoomImageView(context, attrs, defStyleAttr) {

    init {
        setMinZoom(1f)
    }

    var zoomInfo: ZoomInfo? = null
    var mediaScaleTypeContract: MediaScaleTypeContract? = null

    fun initListeners() {
        engine.addListener(object : ZoomEngine.Listener {
            override fun onIdle(engine: ZoomEngine) {

                val bmp = (drawable as? BitmapDrawable)?.bitmap
                if(bmp!=null){
                    this@ZoomAssetImageView.zoomInfo?.let {
                        it.panX = engine.panX
                        it.panY = engine.panY
                        it.scale = engine.zoom
                        it.bmpHeight = bmp.height
                        it.bmpWidth = bmp.width
                    }
                }
            }

            override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {
                //Do nothing
            }
        })
    }

    fun centerCrop() {
        val bmp = (drawable as? BitmapDrawable)?.bitmap
        if (bmp != null) {

            val dwidth = bmp.width
            val dheight = bmp.height

            val vwidth = width - paddingLeft - paddingRight;
            val vheight = height - paddingTop - paddingBottom;

            val scale: Float
            var dx = 0f
            var dy = 0f
            if (dwidth * vheight > vwidth * dheight) {
                scale = (vheight / dheight.toFloat())
                dx = (vwidth - dwidth * scale) * 0.5f
            } else {
                scale = (vwidth / dwidth.toFloat())
                dy = (vheight - dheight * scale) * 0.5f
            }

            engine.moveTo(scale, dx / 2f, dy / 2f, true)
        }
    }

    fun centerInside() {
        engine.moveTo(1f, 0f,0f,true)
    }

    fun loadAsset(asset: Asset, zoomInfo: ZoomInfo) {
        this.zoomInfo = zoomInfo

        Glide.with(this)
            .load(asset.contentUri)
            .fitCenter()
            .addListener(requestListener)
            .into(this)
    }

    val requestListener = object : RequestListener<Drawable?> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            post {
                scaleBitmapOnLoad()
            }
            return false
        }
    }

    fun scaleBitmapOnLoad() {
        if (zoomInfo != null && zoomInfo!!.hasData()) {
            engine.moveTo(zoomInfo!!.scale!!,zoomInfo!!.panX!!,zoomInfo!!.panY!!,false)
        } else if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_CROP) {
            centerCrop()
        } else if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_INSIDE) {
            centerInside()
        }

    }

    fun loadUriThumbnail(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .thumbnail(0.33f)
            .centerCrop()
            .into(this)
    }

    fun loadAssetThumbnail(asset: Asset) {
        loadUriThumbnail(asset.contentUri)
    }

    fun removeAsset() {
        setImageDrawable(null)
    }
}