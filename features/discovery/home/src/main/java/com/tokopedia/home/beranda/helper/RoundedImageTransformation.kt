package com.tokopedia.home.beranda.helper

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest

class RoundedImageTransformation(private val mBitmapPool: BitmapPool, private val mRadius: Int) : Transformation<Bitmap> {
    override fun transform(context: Context, resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        val source = resource.get()

        val width = source.width
        val height = source.height

        var bitmap: Bitmap? = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return BitmapResource.obtain(bitmap, mBitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mDiameter: Int = mRadius * 2

    constructor(context: Context, radius: Int) : this(Glide.get(context).bitmapPool, radius)

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - 0
        val bottom = height - 0

        canvas.drawRoundRect(RectF(right - mDiameter, 0f, right, bottom), mRadius.toFloat(), mRadius.toFloat(),
                paint)
        canvas.drawRect(RectF(0f, 0f, right - mRadius, bottom), paint)
    }
}