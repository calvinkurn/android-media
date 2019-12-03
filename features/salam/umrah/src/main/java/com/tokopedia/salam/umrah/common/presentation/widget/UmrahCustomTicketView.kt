package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.IntDef
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View


import com.tokopedia.salam.umrah.R

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import android.graphics.Bitmap.Config.ALPHA_8
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PorterDuff.Mode.SRC_IN

/**
 * @author by firman on 06/11/2019
 */
class UmrahCustomTicketView : View {


    private val mBackgroundPaint = Paint()
    private val mBorderPaint = Paint()
    private val mPath = Path()
    private val mPathBorder = Path()
    private var mDirty = true

    private val mRoundedCornerArc = RectF()
    private val mScallopCornerArc = RectF()

    private var mBackgroundColor: Int = 0
    private var mShowBorder: Boolean = false
    private var mShowTopShadow: Boolean = false
    private var mShowBottomShadow: Boolean = false
    private var mBorderWidth: Int = 0
    private var mBorderColor: Int = 0
    private var mScallopRadius: Int = 0
    private var mCornerType: Int = 0
    private var mCornerRadius: Int = 0
    private var mShadow: Bitmap? = null
    private val mShadowPaint = Paint(ANTI_ALIAS_FLAG)
    private var mShadowBlurRadius = 0f


    private val isJellyBeanAndAbove: Boolean
        get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1


    @Retention(RetentionPolicy.SOURCE)
    @IntDef(CornerType.NORMAL, CornerType.ALLROUNDED)
    annotation class CornerType {
        companion object {
            const val NORMAL = 0
            const val ALLROUNDED = 1
            const val TOPROUNDEDBOTTOMSCALLOP = 2
            const val BOTTOMROUNDEDTOPSCALLOP = 3
            const val ALLSCALLOP = 4
        }
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDirty) {
            doLayout()
        }
        if (mShadowBlurRadius > 0f && !isInEditMode) {
            canvas.drawBitmap(mShadow!!, 0f, mShadowBlurRadius / 2f, null)
        }
        canvas.drawPath(mPath, mBackgroundPaint)
        if (mShowBorder) {
            canvas.drawPath(mPathBorder, mBorderPaint)
        }
    }


    private fun doLayout() {
        val left = paddingLeft + mShadowBlurRadius
        val right = width.toFloat() - paddingRight.toFloat() - mShadowBlurRadius

        val top: Float
        if (mShowTopShadow)
            top = paddingTop + mShadowBlurRadius / 2
        else
            top = paddingTop.toFloat()

        val bottom: Float
        if (mShowBottomShadow)
            bottom = height.toFloat() - paddingBottom.toFloat() - mShadowBlurRadius - mShadowBlurRadius / 2
        else
            bottom = (height - paddingBottom).toFloat()

        mPath.reset()

        if (mCornerType == CornerType.ALLROUNDED) {
            topRoundedCorners(left, right, top)
            bottomRoundedCorners(left, right, bottom)

        } else if (mCornerType == CornerType.ALLSCALLOP) {
            topScallopCorners(left, right, top)
            bottomScallopCorners(left, right, bottom)

        } else if (mCornerType == CornerType.TOPROUNDEDBOTTOMSCALLOP) {
            topRoundedCorners(left, right, top)
            bottomScallopCorners(left, right, bottom)

        } else if (mCornerType == CornerType.BOTTOMROUNDEDTOPSCALLOP) {
            topScallopCorners(left, right, top)
            bottomRoundedCorners(left, right, bottom)

        } else {
            mPath.moveTo(left, top)
            mPath.lineTo(right, top)
        }

        generateShadow()
        mDirty = false
    }

    private fun topRoundedCorners(left: Float, right: Float, top: Float) {
        mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
        mPath.lineTo(left + mCornerRadius, top)
        mPath.lineTo(right - mCornerRadius, top)
        mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false)
        mPathBorder.addPath(mPath)

    }

    private fun bottomRoundedCorners(left: Float, right: Float, bottom: Float) {

        mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false)
        mPath.lineTo(right - mCornerRadius, bottom)

        mPath.lineTo(left + mCornerRadius, bottom)
        mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false)
    }

    private fun topScallopCorners(left: Float, right: Float, top: Float) {
        mPath.arcTo(getTopLeftCornerScallopArc(top, left), 90.0f, -90.0f, false)
        mPath.lineTo(left + mScallopRadius, top)
        mPath.lineTo(right - mScallopRadius, top)
        mPath.arcTo(getTopRightCornerScallopArc(top, right), 180.0f, -90.0f, false)

    }

    private fun bottomScallopCorners(left: Float, right: Float, bottom: Float) {

        mPath.arcTo(getBottomRightCornerScallopArc(bottom, right), 270.0f, -90.0f, false)
        mPath.lineTo(right - mScallopRadius, bottom)

        mPath.lineTo(left + mScallopRadius, bottom)
        mPath.arcTo(getBottomLeftCornerScallopArc(left, bottom), 0.0f, -90.0f, false)
    }


    private fun generateShadow() {
        if (isJellyBeanAndAbove && !isInEditMode) {
            if (mShadowBlurRadius == 0f) return

            if (mShadow == null) {
                mShadow = Bitmap.createBitmap(width, height, ALPHA_8)
            } else {
                mShadow!!.eraseColor(TRANSPARENT)
            }
            val c = Canvas(mShadow!!)
            c.drawPath(mPath, mShadowPaint)
            if (mShowBorder) {
                c.drawPath(mPath, mShadowPaint)
            }
            val rs = RenderScript.create(context)
            val blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs))
            val input = Allocation.createFromBitmap(rs, mShadow)
            val output = Allocation.createTyped(rs, input.type)
            blur.setRadius(mShadowBlurRadius)
            blur.setInput(input)
            blur.forEach(output)
            output.copyTo(mShadow)
            input.destroy()
            output.destroy()
            blur.destroy()
        }
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UmrahCustomTicketView)
            mBackgroundColor = typedArray.getColor(R.styleable.UmrahCustomTicketView_backgroundColor, resources.getColor(android.R.color.white))
            mScallopRadius = typedArray.getDimensionPixelSize(R.styleable.UmrahCustomTicketView_scallopRadius, dpToPx(20f, context))
            mShowBorder = typedArray.getBoolean(R.styleable.UmrahCustomTicketView_showBorder, false)
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.UmrahCustomTicketView_borderWidth, dpToPx(2f, context))
            mBorderColor = typedArray.getColor(R.styleable.UmrahCustomTicketView_borderColor, resources.getColor(android.R.color.black))
            mCornerType = typedArray.getInt(R.styleable.UmrahCustomTicketView_umrahCornerType, CornerType.NORMAL)
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.UmrahCustomTicketView_cornerRadius, dpToPx(4f, context))
            mShowTopShadow = typedArray.getBoolean(R.styleable.UmrahCustomTicketView_showTopShadow, false)
            mShowBottomShadow = typedArray.getBoolean(R.styleable.UmrahCustomTicketView_showBottomShadow, false)

            var elevation = 0f
            if (typedArray.hasValue(R.styleable.UmrahCustomTicketView_ticketElevation)) {
                elevation = typedArray.getDimension(R.styleable.UmrahCustomTicketView_ticketElevation, elevation)
            }
            if (elevation > 0f) {
                setShadowBlurRadius(elevation)
            }

            typedArray.recycle()
        }

        mShadowPaint.setColorFilter(PorterDuffColorFilter(BLACK, SRC_IN))
        mShadowPaint.setAlpha(51) // 20%

        initElements()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun initElements() {


        setBackgroundPaint()
        setBorderPaint()
        mDirty = true
        invalidate()
    }

    private fun setBackgroundPaint() {
        mBackgroundPaint.alpha = 0
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = mBackgroundColor
        mBackgroundPaint.style = Paint.Style.FILL
    }

    private fun setBorderPaint() {
        mBorderPaint.alpha = 0
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()
        mBorderPaint.style = Paint.Style.STROKE
    }


    private fun getTopLeftCornerRoundedArc(top: Float, left: Float): RectF {
        mRoundedCornerArc.set(left, top, left + mCornerRadius * 2, top + mCornerRadius * 2)
        return mRoundedCornerArc
    }

    private fun getTopRightCornerRoundedArc(top: Float, right: Float): RectF {
        mRoundedCornerArc.set(right - mCornerRadius * 2, top, right, top + mCornerRadius * 2)
        return mRoundedCornerArc
    }

    private fun getBottomLeftCornerRoundedArc(left: Float, bottom: Float): RectF {
        mRoundedCornerArc.set(left, bottom - mCornerRadius * 2, left + mCornerRadius * 2, bottom)
        return mRoundedCornerArc
    }

    private fun getBottomRightCornerRoundedArc(bottom: Float, right: Float): RectF {
        mRoundedCornerArc.set(right - mCornerRadius * 2, bottom - mCornerRadius * 2, right, bottom)
        return mRoundedCornerArc
    }

    private fun getTopLeftCornerScallopArc(top: Float, left: Float): RectF {
        mScallopCornerArc.set(left - mScallopRadius, top - mScallopRadius, left + mScallopRadius, top + mScallopRadius)
        return mScallopCornerArc
    }

    private fun getTopRightCornerScallopArc(top: Float, right: Float): RectF {
        mScallopCornerArc.set(right - mScallopRadius, top - mScallopRadius, right + mScallopRadius, top + mScallopRadius)
        return mScallopCornerArc
    }

    private fun getBottomLeftCornerScallopArc(left: Float, bottom: Float): RectF {
        mScallopCornerArc.set(left - mScallopRadius, bottom - mScallopRadius, left + mScallopRadius, bottom + mScallopRadius)
        return mScallopCornerArc
    }

    private fun getBottomRightCornerScallopArc(bottom: Float, right: Float): RectF {
        mScallopCornerArc.set(right - mScallopRadius, bottom - mScallopRadius, right + mScallopRadius, bottom + mScallopRadius)
        return mScallopCornerArc
    }

    fun getBackgroundColor(): Int {
        return mBackgroundColor
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.mBackgroundColor = backgroundColor
        initElements()
    }

    fun setTicketElevation(elevation: Float) {
        if (!isJellyBeanAndAbove) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above")
            return
        }
        setShadowBlurRadius(elevation)
        initElements()
    }

    private fun setShadowBlurRadius(elevation: Float) {
        if (!isJellyBeanAndAbove) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above")
            return
        }
        val maxElevation = dpToPx(24f, context).toFloat()
        mShadowBlurRadius = Math.min(25f * (elevation / maxElevation), 25f)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return dpToPx(dp, context.resources)
    }

    private fun dpToPx(dp: Float, resources: Resources): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        return px.toInt()
    }

    companion object {

        val TAG = UmrahCustomTicketView::class.java.simpleName
    }
}