package com.tokopedia.promocheckout.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.tokopedia.promocheckout.R

class PromoTicketView : View {

    private var mDividerStartX: Float = 0F
    private var mDividerStartY: Float = 0F
    private val mDividerPaint = Paint()
    private var mDividerStopX: Float = 0F
    private var mDividerStopY: Float = 0F
    private var mBackgroundColor: Int = 0
    private var mShowBorder: Boolean = true
    private var mBorderWidth: Int = 0
    private var mCornerRadius: Int = 0
    private var mScallopRadius: Int = 0
    private var mOffset: Int = 0
    private var mOffsetafter: Int = 0
    private var mCardHeight: Int = 0
    private var mScallopHeight: Int = 0
    private var mShadowblur: Int = 0
    private val rectF = RectF()
    private val mPaint = Paint()
    private val mPath = Path()
    private var mDirty = true

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PromoTicketView)
            mBackgroundColor = typedArray.getColor(R.styleable.PromoTicketView_backgroundcolor, resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            mScallopRadius = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_scallopRadius, convertDpToPx(8).toInt())
            mShowBorder = typedArray.getBoolean(R.styleable.PromoTicketView_showborder, true)
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_borderwidth, convertDpToPx(2).toInt())
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_cornerradius, convertDpToPx(8).toInt())
            mOffset = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_offset, convertDpToPx(158).toInt())
            mOffsetafter = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_offsetafter, convertDpToPx(90).toInt())
            mCardHeight = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_cardheight, convertDpToPx(90).toInt())
            mShadowblur = typedArray.getDimensionPixelSize(R.styleable.PromoTicketView_shadowblur, convertDpToPx(8).toInt())
            mScallopHeight = mScallopRadius * 2

            typedArray.recycle()
        }
        initElements()
    }

    private fun initElements() {

        setBackgroundPaint()
        setBorderPaint()
        mDirty = true
        invalidate()
    }

    private fun setBorderPaint() {
        mDividerPaint.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100)
        mDividerPaint.strokeWidth = resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_2)
        mDividerPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 1.0.toFloat())
    }

    private fun setBackgroundPaint() {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        mPaint.isAntiAlias = true
        mPaint.setShadowLayer(mShadowblur.toFloat() / 2F, 0F, 0F, resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100))
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mDirty) {
            drawTicket()
        }
        canvas?.drawPath(mPath, mPaint)
        if (mShowBorder) {
            canvas?.drawLine(mDividerStartX, mDividerStartY, mDividerStopX, mDividerStopY, mDividerPaint)
        }
    }

    fun drawTicket() {

        val left = paddingLeft + mShadowblur
        val top = paddingTop + mShadowblur

        mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
        rectF.set(
                (left + mOffset).toFloat(),
                (top - mScallopRadius).toFloat(),
                mScallopHeight.toFloat() + mOffset + left,
                (top + mScallopRadius).toFloat()
        )

        mPath.arcTo(rectF, 180.0f, -180.0f, false)
        mPath.arcTo(getTopRightCornerRoundedArc(top, (mScallopHeight + mOffset + left + mOffsetafter)), 270.0f, 90.0f, false
        )
        mPath.arcTo(getBottomRightCornerRoundedArc((top + mCardHeight), (mScallopHeight + mOffset + left + mOffsetafter)), 0F, 90F, false
        )

        rectF.set((left + mOffset).toFloat(),
                (top - mScallopRadius + mCardHeight).toFloat(),
                mScallopHeight.toFloat() + mOffset + left,
                (top + mCardHeight + mScallopRadius).toFloat()
        )
        mPath.arcTo(rectF, 0f, -180.0f, false)
        mPath.arcTo(getBottomLeftCornerRoundedArc(left, (top + mCardHeight)), 90F, 90F, false)
        mPath.close()

        mDividerStartX = (mCornerRadius + left + mOffset).toFloat();
        mDividerStartY = (top + mScallopRadius).toFloat();
        mDividerStopX = (mCornerRadius + left + mOffset).toFloat();
        mDividerStopY = (top + mCardHeight - mScallopRadius).toFloat()

    }

    fun getTopLeftCornerRoundedArc(left: Int, top: Int): RectF {
        rectF.set(left.toFloat(), top.toFloat(), (left + mCornerRadius * 2).toFloat(), (top + mCornerRadius * 2).toFloat())
        return rectF
    }

    fun getTopRightCornerRoundedArc(top: Int, right: Int): RectF {
        rectF.set((right - mCornerRadius * 2).toFloat(), top.toFloat(), right.toFloat(), (top + mCornerRadius * 2).toFloat())
        return rectF
    }

    private fun getBottomLeftCornerRoundedArc(left: Int, bottom: Int): RectF {
        rectF.set(left.toFloat(), (bottom - mCornerRadius * 2).toFloat(), (left + mCornerRadius * 2).toFloat(), bottom.toFloat())
        return rectF
    }

    private fun getBottomRightCornerRoundedArc(bottom: Int, right: Int): RectF {
        rectF.set((right - mCornerRadius * 2).toFloat(), (bottom - mCornerRadius * 2).toFloat(), right.toFloat(), bottom.toFloat())
        return rectF
    }

    private fun convertDpToPx(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }

}
