package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.merchantvoucher.R
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.graphics.Typeface
import android.text.TextPaint
import androidx.annotation.ColorInt


/*
    Provides the background for voucher view
    +-------------------------------------+
    |                                     |
    +-+                                 +-+
      |                                 |
    +-+                                 +-+
    |                                     |
    +-+                                 +-+
      |                                 |
    +-+                                 +-+
    |                                     |
    +-------------------------------------+
 */
open class CustomVoucherView : FrameLayout {

    private val pathSmoothJaggedEdgeBackgroundLayout: Path by lazy {
        Path()
    }
    private val pathSmoothJaggedEdgeOverlayLayout: Path by lazy {
        Path()
    }
    private val paint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    private var requiresShapeUpdate = true
    private var mShadowRadius: Int = 0
    private var totalJaggedEdge: Int = 0
    private var canvasWidth = -1
    private var canvasHeight = -1
    private var minX = -1
    private var maxX = -1
    private var minY = -1
    private var maxY = -1
    private var canvasCleanHeight = -1
    private var totalJaggedAndStraight = -1
    private var diameter = -1f

    companion object {
        private const val DEFAULT_TOTAL_JAGGED_EDGE = 8
        private const val LEFT_JAGGED_BOTTOM_TO_TOP = "LEFT_JAGGED_BOTTOM_TO_TOP"
        private const val RIGHT_JAGGED_TOP_TO_BOTTOM = "RIGHT_JAGGED_TOP_TO_BOTTOM"

    }

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
            setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun applyAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomVoucherView)
            totalJaggedEdge = attributes.getInt(
                    R.styleable.CustomVoucherView_vchTotalJaggedEdge,
                    DEFAULT_TOTAL_JAGGED_EDGE
            )
            if (attributes.hasValue(R.styleable.CustomVoucherView_vchElevation)) {
                mShadowRadius = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchElevation, 0)
            }
            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = androidx.core.content.ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_N0)
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeJoin = Paint.Join.BEVEL
        paint.setShadowLayer(getShadowRadiusValue(), 0f, getShadowOffsetDx(), getShadowColor())
        canvas.drawPath(pathSmoothJaggedEdgeBackgroundLayout, paint)
        super.onDraw(canvas)
    }

    protected open fun getShadowRadiusValue() = 10f

    protected open fun getShadowOffsetDx() = 5f

    @ColorInt
    protected open fun getShadowColor(): Int {
        return androidx.core.content.ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_N300)
    }

    private fun initDimensionData() {
        canvasWidth = width
        canvasHeight = height
        minX = paddingLeft
        maxX = canvasWidth - paddingRight
        minY = paddingTop
        maxY = canvasHeight - paddingBottom - maxYModifier()
        canvasCleanHeight = maxY - minY
        totalJaggedAndStraight = totalJaggedEdge * 2 + 1
        diameter = canvasCleanHeight / totalJaggedAndStraight.toFloat()
    }

    protected open fun maxYModifier(): Int = 0

    override fun dispatchDraw(canvas: Canvas) {
        val savedCanvas = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        paint.reset()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawPath(pathSmoothJaggedEdgeOverlayLayout, paint)
        canvas.restoreToCount(savedCanvas)
        paint.xfermode = null
    }

    private fun drawSmoothJagged(path: Path, smoothJaggedSource: String) {
        var startAngle = 0f
        var sweepAngle = 0f
        when (smoothJaggedSource) {
            LEFT_JAGGED_BOTTOM_TO_TOP -> {
                startAngle = 90f
                sweepAngle = -180f
            }
            RIGHT_JAGGED_TOP_TO_BOTTOM -> {
                startAngle = 270f
                sweepAngle = -180f
            }
        }
        (1..totalJaggedAndStraight).forEach { idx ->
            if (isOddNumber(idx)) {
                var lineToX = 0f
                var lineToY = 0f
                when (smoothJaggedSource) {
                    LEFT_JAGGED_BOTTOM_TO_TOP -> {
                        lineToX = minX.toFloat()
                        lineToY = maxY.toFloat() - (diameter * idx)
                    }
                    RIGHT_JAGGED_TOP_TO_BOTTOM -> {
                        lineToX = maxX.toFloat()
                        lineToY = minY.toFloat() + (diameter * idx)
                    }
                }
                path.lineTo(lineToX, lineToY)
            } else {
                var left = 0f
                var top = 0f
                var right = 0f
                var bottom = 0f
                when (smoothJaggedSource) {
                    LEFT_JAGGED_BOTTOM_TO_TOP -> {
                        left = minX.toFloat() - (diameter / 2f)
                        top = (maxY.toFloat() - (diameter * idx))
                        right = minX.toFloat() + (diameter / 2f)
                        bottom = (maxY.toFloat() - (diameter * idx) + diameter)
                    }
                    RIGHT_JAGGED_TOP_TO_BOTTOM -> {
                        left = maxX.toFloat() - (diameter / 2f)
                        top = (minY.toFloat() + (diameter * idx) - diameter)
                        right = maxX.toFloat() + (diameter / 2f)
                        bottom = (minY.toFloat() + (diameter * idx))
                    }
                }
                path.arcTo(
                        RectF(left, top, right, bottom),
                        startAngle,
                        sweepAngle
                )
            }
        }
    }

    private fun isOddNumber(idx: Int): Boolean = idx % 2 != 0

    private fun initSmoothJaggedEdgePathBackground() {
        if (requiresShapeUpdate && width > 0) {
            pathSmoothJaggedEdgeBackgroundLayout.reset()
            pathSmoothJaggedEdgeBackgroundLayout.moveTo(minX.toFloat(), minY.toFloat())
            pathSmoothJaggedEdgeBackgroundLayout.lineTo(maxX.toFloat(), minY.toFloat())
            drawSmoothJagged(pathSmoothJaggedEdgeBackgroundLayout, RIGHT_JAGGED_TOP_TO_BOTTOM)
            pathSmoothJaggedEdgeBackgroundLayout.lineTo(maxX.toFloat(), maxY.toFloat() + maxYModifier())
            pathSmoothJaggedEdgeBackgroundLayout.lineTo(minX.toFloat(), maxY.toFloat() + maxYModifier())
            drawSmoothJagged(pathSmoothJaggedEdgeBackgroundLayout, LEFT_JAGGED_BOTTOM_TO_TOP)
            pathSmoothJaggedEdgeBackgroundLayout.lineTo(minX.toFloat(), minY.toFloat())
            pathSmoothJaggedEdgeBackgroundLayout.close()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initDimensionData()
        initSmoothJaggedEdgePathBackground() // draw shadow
        initSmoothJaggedEdgePathOverlay() // draw jagged path
        if (changed) {
            this.requiresShapeUpdate = true
            postInvalidate()
        }
    }

    private fun initSmoothJaggedEdgePathOverlay() {
        pathSmoothJaggedEdgeOverlayLayout.reset()
        pathSmoothJaggedEdgeOverlayLayout.moveTo(maxX.toFloat(), minY.toFloat())
        drawSmoothJagged(pathSmoothJaggedEdgeOverlayLayout, RIGHT_JAGGED_TOP_TO_BOTTOM)
        pathSmoothJaggedEdgeOverlayLayout.lineTo(maxX.toFloat(), maxY.toFloat())
        pathSmoothJaggedEdgeOverlayLayout.close()
        pathSmoothJaggedEdgeOverlayLayout.moveTo(minX.toFloat(), minY.toFloat())
        drawSmoothJagged(pathSmoothJaggedEdgeOverlayLayout, LEFT_JAGGED_BOTTOM_TO_TOP)
        pathSmoothJaggedEdgeOverlayLayout.lineTo(minX.toFloat(), maxY.toFloat())
        pathSmoothJaggedEdgeOverlayLayout.close()
    }

    protected fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()
    }

    inner class SpanText(
            stringSource: String,
            stringToBeSpanned: String
    ) {
        private val spannableString: SpannableString = SpannableString(stringSource)
        private val startIndex = stringSource.indexOf(stringToBeSpanned)
        private val endIndex = startIndex + stringToBeSpanned.length

        fun addBoldSpanWithFontFamily(fontFamilyName: String): SpanText {
            if (startIndex == -1)
                return this
            if (fontFamilyName.isNotEmpty()) {
                spannableString.setSpan(
                        TypefaceSpan(fontFamilyName),
                        startIndex,
                        endIndex,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            return this
        }

        fun changeTextSize(sizeInDp: Int): SpanText {
            if (startIndex == -1)
                return this
            spannableString.setSpan(
                    AbsoluteSizeSpan(sizeInDp),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            return this
        }

        fun getCharSequence(): CharSequence {
            return spannableString
        }


    }

    inner class CustomTypefaceSpan(family: String, private val newType: Typeface) : TypefaceSpan(family) {

        override fun updateDrawState(ds: TextPaint) {
            applyCustomTypeFace(ds, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            applyCustomTypeFace(paint, newType)
        }

        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old = paint.typeface
            if (old == null) {
                oldStyle = 0
            } else {
                oldStyle = old.style
            }

            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }

            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }

            paint.typeface = tf
        }
    }


}