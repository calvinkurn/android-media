package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx

open class CouponConstraintLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val porterDuff = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val clipPath = Path()
    private val clipRectF = RectF()

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(2)
        paint.color = Color.TRANSPARENT
        paint.xfermode = porterDuff
        paint.isAntiAlias = true
    }

    override fun dispatchDraw(canvas: Canvas) {
        val cornerRadius = dpToPx(8)

        clipPath.reset()
        clipRectF.top = 0f
        clipRectF.left = 0f
        clipRectF.right = canvas.width.toFloat()
        clipRectF.bottom = canvas.height.toFloat()
        clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW)

        canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)
    }
}