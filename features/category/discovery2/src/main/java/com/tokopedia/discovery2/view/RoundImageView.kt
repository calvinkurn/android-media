package com.tokopedia.discovery2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Color.parseColor

class RoundImageView : AppCompatImageView {

    protected var clipPath = Path()
    protected var clipRectF = RectF()
    private var cornerRadius = 18f

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context) : super(context)


//    override fun onDraw(canvas: Canvas) {
//        val drawable = drawable ?: return
//
//        if (width == 0 || height == 0) {
//            return
//        }
//        val b = (drawable as BitmapDrawable).bitmap
//        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
//
//        val w = width
//        val h = height
//
//        val roundBitmap = getRoundedCroppedBitmap(bitmap, w)
//        canvas.drawBitmap(roundBitmap, 0, 0, null)
//    }
//
//    fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
//        val finalBitmap: Bitmap
//        if (bitmap.width != radius || bitmap.height != radius)
//            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
//                    false)
//        else
//            finalBitmap = bitmap
//        val output = Bitmap.createBitmap(finalBitmap.width,
//                finalBitmap.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(output)
//
//        val paint = Paint()
//        val rect = Rect(0, 0, finalBitmap.width,
//                finalBitmap.height)
//
//        paint.setAntiAlias(true)
//        paint.setFilterBitmap(true)
//        paint.setDither(true)
//        canvas.drawARGB(0, 0, 0, 0)
//        paint.setColor(Color.parseColor("#BAB399"))
//        canvas.drawCircle(finalBitmap.width / 2 + 0.7f,
//                finalBitmap.height / 2 + 0.7f,
//                finalBitmap.width / 2 + 0.1f, paint)
//        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
//        canvas.drawBitmap(finalBitmap, rect, rect, paint)
//
//        return output
//    }

    fun makeRound(canvas: Canvas) {
        clipPath.reset()

        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = width.toFloat()
        clipRectF.bottom = height.toFloat()
        clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)
    }


}