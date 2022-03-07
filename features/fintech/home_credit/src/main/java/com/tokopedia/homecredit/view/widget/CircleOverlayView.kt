package com.tokopedia.homecredit.view.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tokopedia.homecredit.R
import kotlin.math.abs

class CircleOverlayView : LinearLayout {
    private var bitmap: Bitmap? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (bitmap == null) {
            createBitmap()
        }
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    private fun createBitmap() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val osCanvas = Canvas(bitmap ?: return)
        val mainRectangle = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val ovalWidth =
            (width / 2 + resources.getDimensionPixelSize(R.dimen.hc_oval_width)).toFloat()
        val ovalHeight =
            (height / 2 - resources.getDimensionPixelSize(R.dimen.hc_oval_height)).toFloat()
        val ktpRectWidth = ovalWidth - resources.getDimensionPixelSize(R.dimen.hc_oval_width)
        val ktpRectHeight = resources.getDimensionPixelSize(R.dimen.hc_ktp_height).toFloat()
        val ovalAndKtpRectDiffHeight =
            resources.getDimensionPixelSize(R.dimen.hc_oval_width).toFloat()
        val outerRectWidth = ovalWidth + resources.getDimensionPixelSize(R.dimen.hc_oval_width)
        val outerRectHeight =
            ovalHeight + ktpRectHeight + ovalAndKtpRectDiffHeight + resources.getDimensionPixelSize(
                R.dimen.hc_oval_width
            ) //100px is gap b/w oval and ktprect
        val ovalVerticleShift =
            abs((ovalHeight + ktpRectHeight + ovalAndKtpRectDiffHeight) / 2 - ovalHeight)
        val outerRectangle = RectF(
            width / 2 - outerRectWidth / 2,
            height / 2 - outerRectHeight / 2,
            width / 2 + outerRectWidth / 2,
            height / 2 + outerRectHeight / 2
        )
        val innerRectangle = RectF(
            width / 2 - ovalWidth / 2,
            height / 2 - ovalHeight + ovalVerticleShift,
            width / 2 + ovalWidth / 2,
            height / 2 + ovalVerticleShift
        )
        val ktpRectangle = RectF(
            width / 2 - ktpRectWidth / 2,
            height / 2 + ovalAndKtpRectDiffHeight + ovalVerticleShift,
            width / 2 + ktpRectWidth / 2,
            height / 2 + ktpRectHeight + ovalAndKtpRectDiffHeight + ovalVerticleShift
        )
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500)
        paint.alpha = 255
        osCanvas.drawRect(mainRectangle, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        paint.alpha = 49
        osCanvas.drawRect(outerRectangle, paint)
        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        osCanvas.drawOval(innerRectangle, paint)
        osCanvas.drawRoundRect(ktpRectangle, 10f, 10f, paint)
    }
}