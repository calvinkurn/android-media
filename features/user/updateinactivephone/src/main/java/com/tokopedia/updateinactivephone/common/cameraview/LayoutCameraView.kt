package com.tokopedia.updateinactivephone.common.cameraview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.updateinactivephone.R

class LayoutCameraView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paintWhite = Paint()
    private val path = Path()

    var layoutType: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        paintWhite.color = Color.WHITE
        paintWhite.style = Paint.Style.STROKE
        paintWhite.strokeWidth = STROKE_WIDTH

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.LayoutCameraView, 0, 0)
            typedArray?.let {
                layoutType = it.getInt(R.styleable.LayoutCameraView_layoutType, 0)
                it.recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        path.reset()

        if (layoutType != 0) {
            when (layoutType) {
                CameraViewMode.ID_CARD.id -> { drawIdCardLayout(canvas) }
                CameraViewMode.SELFIE.id -> { drawSelfieLayout(canvas) }
            }

            canvas?.clipPath(path)
            canvas?.drawColor(ContextCompat.getColor(context, R.color.overlay_color))
        }
    }

    /**
     * Draw layout for capture id card
     * */
    private fun drawIdCardLayout(canvas: Canvas?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            path.addRect(createIdCardRect(), Path.Direction.CW)
        } else {
            path.addRoundRect(createIdCardRect(), RADIUS, RADIUS, Path.Direction.CW)
        }

        path.fillType = Path.FillType.INVERSE_EVEN_ODD

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            canvas?.drawRect(createIdCardRect(), paintWhite)
        } else {
            canvas?.drawRoundRect(createIdCardRect(), RADIUS, RADIUS, paintWhite)
        }
    }

    private fun createIdCardRect(): RectF {
        return RectF(
                left + (right - left) / DIMEN_ID_CARD_LEFT,
                top + (bottom - top) / DIMEN_ID_CARD_TOP,
                right - (right - left) / DIMEN_ID_CARD_RIGHT,
                bottom - (bottom - top) / DIMEN_ID_CARD_BOTTOM
        )
    }

    /**
     * Draw layout for capture selfie
     * */
    private fun drawSelfieLayout(canvas: Canvas?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            path.addRect(createRectSelfie(), Path.Direction.CW)
        } else {
            path.addOval(createOvalSelfie(), Path.Direction.CW)
            path.addRoundRect(createRoundRectSelfie(), RADIUS, RADIUS, Path.Direction.CW)
        }

        path.fillType = Path.FillType.INVERSE_EVEN_ODD

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            canvas?.drawRect(createRectSelfie(), paintWhite)
        } else {
            canvas?.drawOval(createOvalSelfie(), paintWhite)
            canvas?.drawRoundRect(createRoundRectSelfie(), RADIUS, RADIUS, paintWhite)
        }
    }

    private fun createRectSelfie(): RectF {
        return RectF(
                left + (right - left) / DIMEN_LEFT_KK,
                top + (bottom - top) / DIMEN_TOP_KK,
                right - (right - left) / DIMEN_RIGHT_KK,
                bottom - (bottom - top) / DIMEN_BOTTOM_KK
        )
    }

    private fun createOvalSelfie(): RectF {
        return RectF(
                left + (right - left) / DIMEN_LEFT_OVAL,
                top + (bottom - top) / DIMEN_TOP_OVAL,
                right - (right - left) / DIMEN_RIGHT_OVAL,
                bottom - (bottom - top) / DIMEN_BOTTOM_OVAL
        )
    }

    private fun createRoundRectSelfie(): RectF {
        return RectF(
                left + (right - left) / DIMEN_LEFT,
                top + (bottom - top) / DIMEN_TOP,
                right - (right - left) / DIMEN_RIGHT,
                bottom - (bottom - top) / DIMEN_BOTTOM
        )
    }

    companion object {
        private const val DIMEN_LEFT_KK = 20f
        private const val DIMEN_TOP_KK = 3.22f
        private const val DIMEN_RIGHT_KK = 20f
        private const val DIMEN_BOTTOM_KK = 3.22f

        private const val DIMEN_LEFT_OVAL = 1.25f
        private const val DIMEN_TOP_OVAL = 1.90f
        private const val DIMEN_RIGHT_OVAL = 1.25f
        private const val DIMEN_BOTTOM_OVAL = 1.15f

        private const val DIMEN_LEFT = 1.25f
        private const val DIMEN_TOP = 1.35f
        private const val DIMEN_RIGHT = 1.25f
        private const val DIMEN_BOTTOM = 2.20f

        private const val DIMEN_ID_CARD_LEFT = 20f
        private const val DIMEN_ID_CARD_TOP = 3f
        private const val DIMEN_ID_CARD_RIGHT = 20f
        private const val DIMEN_ID_CARD_BOTTOM = 3f

        private const val RADIUS = 30f
        private const val STROKE_WIDTH = 10f
    }
}