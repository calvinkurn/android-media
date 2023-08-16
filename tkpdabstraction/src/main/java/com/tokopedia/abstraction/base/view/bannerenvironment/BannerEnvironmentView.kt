package com.tokopedia.abstraction.base.view.bannerenvironment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.tokopedia.abstraction.R

internal class BannerEnvironmentView(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val paint = Paint().apply { color = context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500) }
    private val path: Path = Path()
    private val textView: TextView by lazy { TextView(context) }
    private val bannerHeight: Float by lazy { dip(30).toFloat() }
    var bannerGravity: BannerEnvironmentGravity = BannerEnvironmentGravity.START

    init {
        textView.apply {
            setTextColor(Color.BLACK)
            includeFontPadding = false
            rotation = -45f
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            textSize = 11.5f
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.END
            setPadding(0, 0, dip(5), 0)
        }
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER)
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
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(
            path.apply {
                moveTo(width - bannerHeight, 0f)
                lineTo(width.toFloat(), 0f)
                lineTo(0f, height.toFloat())
                lineTo(0f, height - bannerHeight)
                close()
            },
            paint
        )
    }

    private fun View.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
