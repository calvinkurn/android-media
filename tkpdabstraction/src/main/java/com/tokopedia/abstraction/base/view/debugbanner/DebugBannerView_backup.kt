package com.tokopedia.manageaddress.ui.debugbanner

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.tokopedia.abstraction.base.view.debugbanner.BannerGravity
import io.armcha.debugBanner.dip
import io.armcha.debugBanner.getScreenWidth

internal class DebugBannerView_backup(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.RED }
    private val path: Path = Path()
    private val textView: TextView by lazy { TextView(context) }
    private val bannerHeight: Float by lazy { dip(30).toFloat() }
    var bannerGravity: BannerGravity = BannerGravity.START

    init {
        textView.apply {
            setTextColor(Color.BLACK)
            includeFontPadding = false
//            rotation = -45f
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            textSize = 10f
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.END
        }
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
        layoutParams.setMargins(dip(5), 0, dip(5), 0)
        addView(textView, layoutParams)
        setBackgroundColor(Color.TRANSPARENT)
        ViewCompat.setElevation(this, 30f)
        isClickable = false
    }

    fun updateText(text: String, textColor: Int) {
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(context, textColor))
    }

    fun updateBannerColor(bannerColor: Int) {
        paint.color = ContextCompat.getColor(context, bannerColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (textView.x == 0f) {
            textView.x = -bannerHeight / 4
            textView.y = -bannerHeight / 4
            if (bannerGravity == BannerGravity.END) {
//                rotation = 90f
                translationX = context.getScreenWidth() - measuredHeight
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
//        canvas?.drawPath(path.apply {
// //            moveTo(width - bannerHeight, 0f)
// //            lineTo(width.toFloat(), 0f)
// //            lineTo(0f, height.toFloat())
// //            lineTo(0f, height - bannerHeight)
//            rect
//            close()
//        }, paint)
        canvas?.drawRect(
            0f,
            0f,
            getScreenWidth().toFloat(),
            statusBarHeight(resources).toFloat(),
            paint
        )
    }

    private fun statusBarHeight(res: Resources): Int {
        return (24 * res.displayMetrics.density).toInt()
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }
}
