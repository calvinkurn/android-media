package com.tokopedia.imagepicker_insta.views

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.otaliastudios.zoom.ZoomEngine
import com.otaliastudios.zoom.ZoomImageView
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import kotlin.math.max
import kotlin.math.min

class ZoomAssetImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ZoomImageView(context, attrs, defStyleAttr) {

    init {
        setMinZoom(1f)
    }

    var zoomInfo: ZoomInfo? = null
    var mediaScaleTypeContract: MediaScaleTypeContract? = null
    private val portraitAR = 4 / 5f
    private val landscapeAR = 16 / 9f

    fun lockMinZoom() {
        //Do nothing
    }

    fun unLockMinZoom() {
        //Do nothing
    }

    fun updateZoomInfo(bmp: Bitmap?, engine: ZoomEngine){
        if(bmp!=null){
            zoomInfo?.let {
                it.panX = engine.panX
                it.panY = engine.panY
                it.scale = engine.zoom
                it.bmpHeight = bmp.height
                it.bmpWidth = bmp.width
            }
        }
    }

    private fun updateZoomInfo(bmp: Bitmap?, scale: Float, panX:Float, panY:Float){
        if(bmp!=null){
            zoomInfo?.let {
                it.panX = panX
                it.panY = panY
                it.scale = scale
                it.bmpHeight = bmp.height
                it.bmpWidth = bmp.width
            }
        }
    }

    fun initListeners() {
        engine.addListener(object : ZoomEngine.Listener {
            override fun onIdle(engine: ZoomEngine) {
                val bmp = (drawable as? BitmapDrawable)?.bitmap
                updateZoomInfo(bmp,engine)
            }

            override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {
                //Do nothing
            }
        })
    }

    fun centerCrop(animate: Boolean) {
        val bmp = (drawable as? BitmapDrawable)?.bitmap
        if (bmp != null) {

            val dwidth = bmp.width
            val dheight = bmp.height

            val vwidth = width - paddingLeft - paddingRight;
            val vheight = height - paddingTop - paddingBottom;

            var scale: Float
            var dx = 0f
            var dy = 0f

            if (dwidth * vheight > vwidth * dheight) {
                //Landscape
                scale = (vheight / dheight.toFloat())
                scale = max(scale,engine.getMinZoom())
                dx = (vwidth - dwidth * scale) * 0.5f
            } else {
                //Portrait
                scale = (vwidth / dwidth.toFloat())
                scale = max(scale,engine.getMinZoom())
                dy = (vheight - dheight * scale) * 0.5f
            }

            val panX = dx / 2f
            val panY = dy / 2f
            engine.moveTo(scale, panX, panY, animate)
            updateZoomInfo(bmp,scale,panX,panY)
        }
    }

    fun centerInside(animate: Boolean) {
        val bmp = (drawable as? BitmapDrawable)?.bitmap
        if (bmp != null) {

            val dwidth = bmp.width
            val dheight = bmp.height

            val vwidth = width - paddingLeft - paddingRight;
            val vheight = height - paddingTop - paddingBottom;

            var dx = 0f
            var dy = 0f
            val scale = engine.getMinZoom()

            if (dwidth * vheight > vwidth * dheight) {
                //Landscape
                dx = (vwidth - dwidth * scale) * 0.5f
            } else {
                //Portrait
                dy = (vheight - dheight * scale) * 0.5f
            }

            val panX = dx / 2f
            val panY = dy / 2f
            engine.moveTo(scale, panX, panY, animate)
            updateZoomInfo(bmp,scale,panX,panY)
        }else{
            engine.moveTo(1f, 0f, 0f, animate)
            updateZoomInfo(bmp,engine)
        }
    }

    private fun isValidGlideContext(context: Context): Boolean {
        if (context is Activity) {
            return !(context.isFinishing || context.isDestroyed)
        }
        return false
    }

    fun loadAsset(asset: Asset, zoomInfo: ZoomInfo) {
        this.zoomInfo = zoomInfo

        if (!isValidGlideContext(context)) return

        Glide.with(this)
            .load(asset.contentUri)
            .fitCenter()
            .addListener(requestListener)
            .into(this)
    }

    val requestListener = object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            updateMinZoom(resource)
            post {
                scaleBitmapOnLoad()
            }
            return false
        }
    }

    fun updateMinZoom(resource: Drawable?) {
        val bmp = (resource as? BitmapDrawable)?.bitmap
        if (bmp != null) {
            val ar = bmp.width / bmp.height.toFloat()

            val targetAR = if (ar > 1) {
                max(landscapeAR * 1 / ar, landscapeAR)
            } else {
                max(portraitAR * 1 / ar, ar)
            }
            val maxZoom = engine.getMaxZoom()
            val finalMinZoom = max(1f, targetAR)
            setMinZoom(min(maxZoom, finalMinZoom))
        }
    }

    fun scaleBitmapOnLoad() {
        if (zoomInfo != null && zoomInfo!!.hasData()) {
            engine.moveTo(zoomInfo!!.scale!!, zoomInfo!!.panX!!, zoomInfo!!.panY!!, false)
        } else if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_CROP) {
            centerCrop(false)
        } else if (mediaScaleTypeContract?.getCurrentMediaScaleType() == MediaScaleType.MEDIA_CENTER_INSIDE) {
            centerInside(false)
        }
    }

    fun removeAsset() {
        setImageDrawable(null)
    }
}