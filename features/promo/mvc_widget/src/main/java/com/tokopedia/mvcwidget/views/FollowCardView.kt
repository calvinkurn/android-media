package com.tokopedia.mvcwidget.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.isDarkMode
import com.tokopedia.promoui.common.dpToPx

const val TYPE_LARGE = 0
const val TYPE_SMALL = 1

open class FollowCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private var w = 0
    private var h = 0
    val paintSmall = Paint()
    val paintLarge = Paint()
    var rectLarge = RectF(0f, 0f, 0f, 0f)
    var rectSmall = RectF(0f, 0f, 0f, 0f)
    var type = TYPE_LARGE

    init {
        paintSmall.isAntiAlias = true
        paintSmall.style = Paint.Style.FILL
        paintLarge.isAntiAlias = true
        paintLarge.style = Paint.Style.FILL
        if (isDarkMode(context)) {
            paintSmall.color =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.dark_N100)
            paintLarge.color =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.dark_N150)

        } else {
            paintSmall.color =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G200)
            paintLarge.color =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G100)
        }
        background.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0),PorterDuff.Mode.SRC)
        readFromAttrs(attrs)
    }

    private fun readFromAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                context.theme.obtainStyledAttributes(it, R.styleable.FollowCardView, 0, 0)
            type = typedArray.getInt(R.styleable.FollowCardView_type, TYPE_LARGE)
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        if (type == TYPE_LARGE) {
            val paddingLarge = dpToPx(36)
            val paddingSmall = dpToPx( 16)
            val circleDiameterLarge = h + paddingLarge
            val circleDiameterSmall = h - paddingSmall
            rectLarge = RectF(
                -w * 0.53f,
                -paddingLarge,
                w * 0.16f,
                circleDiameterLarge
            )
            rectSmall = RectF(
                -w * 0.28f,
                paddingSmall,
                w * 0.0858f,
                circleDiameterSmall
            )
        } else {
            val paddingLarge = dpToPx(80)
            val paddingSmall = dpToPx(28)
            val circleDiameterLarge = h + paddingLarge
            val circleDiameterSmall = h + paddingSmall
            rectLarge = RectF(
                -w * 0.53f,
                -paddingLarge,
                w * 0.16f,
                circleDiameterLarge
            )
            rectSmall = RectF(
                - w * 0.2804f,
                -paddingSmall,
                 w * 0.0858f,
                circleDiameterSmall
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawArc(rectLarge, -90f, 180f, false, paintLarge)
        canvas?.drawArc(rectSmall, -90f, 180f, false, paintSmall)
        super.onDraw(canvas)
    }
}
