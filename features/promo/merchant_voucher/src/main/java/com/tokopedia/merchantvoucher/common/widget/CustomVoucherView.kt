package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.merchantvoucher.R

/*
    Provides the background for voucher view
    +---------------------+   +--------+
    |                     +-+-+        |
    |                       |          |
    |                       |          |
    |                     +-+-+        |
    +---------------------+   +--------+
 */
open class CustomVoucherView : FrameLayout {

    private var path: Path? = null
    private var linePath: Path? = null
    private var borderPaint: Paint? = null
    private var dashPaint: Paint? = null
    private var requiresShapeUpdate = true

    protected var cornerRadius: Int = 0
    protected var mScallopRadius: Int = 0
    protected var mScallopRelativePosition: Float = 0.toFloat()
    protected var mShadowRadius: Int = 0
    protected var mDashWidth: Int = 0
    protected var mDashGap: Int = 0
    protected var mDashColor: Int = 0

    protected var drawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        applyAttrs(context, attrs)

        isDrawingCacheEnabled = true
        setWillNotDraw(false)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, borderPaint)
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun applyAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val defaultRadius = dpToPx(4f)
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomVoucherView)
            cornerRadius = attributes.getDimensionPixelSize(R.styleable.CustomVoucherView_vchCornerRadius, defaultRadius)
            mScallopRadius = attributes.getDimensionPixelSize(R.styleable.CustomVoucherView_vchScallopRadius, defaultRadius * 2)
            mScallopRelativePosition = attributes.getFloat(R.styleable.CustomVoucherView_vchScallopRelativePosition, 0.7f)
            if (attributes.hasValue(R.styleable.CustomVoucherView_vchElevation)) {
                mShadowRadius = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchElevation, 0)
            }
            mDashWidth = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchDashWidth, defaultRadius)
            mDashGap = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchDashGap, defaultRadius)
            mDashColor = attributes.getColor(R.styleable.CustomVoucherView_vchDashColor, resources.getColor(android.R.color.black))
            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        initVoucherPath()
        if (borderPaint == null) {
            borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            borderPaint!!.isAntiAlias = true
            borderPaint!!.color = Color.WHITE
            borderPaint!!.strokeWidth = 1f
            borderPaint!!.style = Paint.Style.FILL_AND_STROKE
            borderPaint!!.strokeJoin = Paint.Join.BEVEL
            borderPaint!!.setShadowLayer(mShadowRadius.toFloat(), 0f, (mShadowRadius / 2).toFloat(), Color.GRAY)
        }

        if (dashPaint == null) {
            dashPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            dashPaint!!.style = Paint.Style.STROKE
            dashPaint!!.pathEffect = DashPathEffect(floatArrayOf(mDashGap.toFloat(), mDashGap.toFloat()), 0f)
            dashPaint!!.strokeWidth = mDashWidth.toFloat()
            dashPaint!!.color = mDashColor
        }

        if (path != null) {
            canvas.drawPath(path!!, borderPaint!!)
        }
        if (linePath != null) {
            canvas.drawPath(linePath!!, dashPaint!!)
        }
        super.onDraw(canvas)
    }

    private fun initVoucherPath() {
        if (requiresShapeUpdate && width > 0) {
            if (path == null) {
                path = Path()
            } else {
                path!!.reset()
            }
            val canvasWidth = width
            val canvasHeight = height
            val minX = paddingLeft
            val maxX = canvasWidth - paddingRight
            val minY = paddingTop
            val maxY = canvasHeight - paddingBottom
            val objectWidth = maxX - minX

            val scallopXCenterPos = mScallopRelativePosition * objectWidth + minX

            path!!.moveTo((minX + cornerRadius).toFloat(), minY.toFloat())
            path!!.lineTo(scallopXCenterPos - mScallopRadius, minY.toFloat())
            path!!.arcTo(RectF(scallopXCenterPos - mScallopRadius,
                    (minY - mScallopRadius).toFloat(), scallopXCenterPos + mScallopRadius,
                    (minY + mScallopRadius).toFloat()),
                    180f, -180f)
            path!!.lineTo((maxX - cornerRadius).toFloat(), minY.toFloat())
            path!!.arcTo(RectF(maxX - cornerRadius * 2f, minY.toFloat(), maxX.toFloat(), minY + cornerRadius * 2f), -90f, 90f)
            path!!.lineTo(maxX.toFloat(), (maxY - cornerRadius).toFloat())
            path!!.arcTo(RectF(maxX - cornerRadius * 2f, maxY - cornerRadius * 2f, maxX.toFloat(), maxY.toFloat()), 0f, 90f)
            path!!.lineTo(scallopXCenterPos + mScallopRadius, maxY.toFloat())
            path!!.arcTo(RectF(scallopXCenterPos - mScallopRadius,
                    (maxY - mScallopRadius).toFloat(), scallopXCenterPos + mScallopRadius,
                    (maxY + mScallopRadius).toFloat()),
                    0f, -180f)
            path!!.lineTo(scallopXCenterPos - mScallopRadius, maxY.toFloat())
            path!!.lineTo((minX + cornerRadius).toFloat(), maxY.toFloat())
            path!!.arcTo(RectF(minX.toFloat(), maxY - cornerRadius * 2f, minX + cornerRadius * 2f, maxY.toFloat()), 90f, 90f)
            path!!.lineTo(minX.toFloat(), (minY + cornerRadius).toFloat())
            path!!.arcTo(RectF(minX.toFloat(), minY.toFloat(), minX + cornerRadius * 2f, minY + cornerRadius * 2f), 180f, 90f)
            path!!.close()

            if (linePath == null) {
                linePath = Path()
            } else {
                linePath!!.reset()
            }
            linePath!!.moveTo(scallopXCenterPos, (minY + mScallopRadius).toFloat())
            linePath!!.lineTo(scallopXCenterPos, (maxY - mScallopRadius).toFloat())
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            this.requiresShapeUpdate = true
            postInvalidate()
        }
    }

    protected fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()
    }


}
