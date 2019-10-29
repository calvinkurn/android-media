package com.tokopedia.promocheckout.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt
import android.renderscript.Element.U8
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.renderscript.Allocation
import android.graphics.Paint.ANTI_ALIAS_FLAG
import com.tokopedia.promocheckout.R
import android.graphics.PorterDuffColorFilter




class PromoTicketView : View {

    private var mDividerStartX: Float = 0F
    private var mDividerStartY: Float = 0F
    private val mDividerPaint = Paint()
    private var mDividerStopX: Float = 0F
    private var mDividerStopY: Float = 0F

    val rectF = RectF()
    val mPaint = Paint()
    val mPath = Path()
    var shadowBlur = 6
    var offset = 158
    var cornerRadius = 8
    var scallopRadius = 8
    var mScallopHeight = 16
    var offsetafter = 84
    var cardheight = 96
    private var mShadow: Bitmap? = null
    private var mShadowPaint = Paint(ANTI_ALIAS_FLAG)


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

        cornerRadius = convertDpToPx(cornerRadius).roundToInt()
        scallopRadius = convertDpToPx(scallopRadius).roundToInt()
        mScallopHeight = scallopRadius * 2
        offset = convertDpToPx(offset).roundToInt()
        offsetafter = convertDpToPx(offsetafter).roundToInt()
        cardheight = convertDpToPx(cardheight).roundToInt()
        shadowBlur = convertDpToPx(shadowBlur).roundToInt()

        with(mDividerPaint) {
            isAntiAlias = true
            color = resources.getColor(R.color.darker_color)
            strokeWidth = resources.getDimension(R.dimen.dp_2)
        }

        with(mPaint) {
            style = Paint.Style.FILL
            color = Color.WHITE
            alpha = 0
            isAntiAlias = true
        }


        mShadowPaint.colorFilter = PorterDuffColorFilter(BLACK, PorterDuff.Mode.SRC_IN)
        mShadowPaint.alpha = 51
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTicket()
      //  canvas?.drawBitmap(mShadow, 0f, shadowBlur / 2f, null);
        canvas?.drawPath(mPath, mPaint)
    //    canvas?.drawLine(mDividerStartX, mDividerStartY, mDividerStopX, mDividerStopY, mDividerPaint)

    }

    fun drawTicket() {

        val left = paddingLeft + shadowBlur
        val right = paddingRight + shadowBlur
        val top = paddingTop + shadowBlur
        val bottom = paddingBottom + shadowBlur


        mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
        rectF.set(
                (left + offset).toFloat(),
                (top - scallopRadius).toFloat(),
                mScallopHeight.toFloat() + offset + left,
                (top + scallopRadius).toFloat()
        )

        mPath.arcTo(rectF, 180.0f, -180.0f, false)
        mPath.arcTo(
                getTopRightCornerRoundedArc(
                        top,
                        (mScallopHeight.toFloat() + offset + left + offsetafter).toInt()
                ), 270.0f, 90.0f, false
        )

        mPath.arcTo(
                getBottomRightCornerRoundedArc(
                        (top + cardheight).toFloat(),
                        (mScallopHeight.toFloat() + offset + left + offsetafter)
                ), 0F, 90F, false
        )


        rectF.set(
                (left + offset).toFloat(),
                (top - scallopRadius + cardheight).toFloat(),
                mScallopHeight.toFloat() + offset + left,
                (top + cardheight + scallopRadius).toFloat()
        )

        mPath.arcTo(rectF, 0f, -180.0f, false)

        //   mPath.moveTo((left+offset).toFloat(), (top+200).toFloat())
        mPath.arcTo(
                getBottomLeftCornerRoundedArc(left.toFloat(), (top + cardheight).toFloat()),
                90F,
                90F,
                false
        )

        mPath.close()

        mDividerStartX = (scallopRadius + left + offset).toFloat();
        mDividerStartY = (top + scallopRadius).toFloat();
        mDividerStopX = (scallopRadius + left + offset).toFloat();
        mDividerStopY = (bottom - scallopRadius).toFloat()

       // generateShadow()

    }

    private fun generateShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            if (mShadow == null) {
                mShadow = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
            } else {
                mShadow?.eraseColor(TRANSPARENT)
            }
            val c = Canvas(mShadow)
            c.drawPath(mPath, mShadowPaint)
            val rs = RenderScript.create(context)
            val blur = ScriptIntrinsicBlur.create(rs, U8(rs))
            val input = Allocation.createFromBitmap(rs, mShadow)
            val output = Allocation.createTyped(rs, input.getType())
            blur.setRadius(shadowBlur.toFloat())

            blur.setInput(input)
            blur.forEach(output)
            output.copyTo(mShadow)
            input.destroy()
            output.destroy()
            blur.destroy()
        }
    }

    fun getTopLeftCornerRoundedArc(left: Int, top: Int): RectF {
        return RectF(
                left.toFloat(),
                top.toFloat(),
                (left + cornerRadius * 2).toFloat(),
                (top + cornerRadius * 2).toFloat()
        )
    }

    fun getTopRightCornerRoundedArc(top: Int, right: Int): RectF {
        return RectF(
                (right - cornerRadius * 2).toFloat(),
                top.toFloat(),
                right.toFloat(),
                (top + cornerRadius * 2).toFloat()
        )
    }

    private fun getBottomLeftCornerRoundedArc(left: Float, bottom: Float): RectF {
        return RectF(left, bottom - cornerRadius * 2, left + cornerRadius * 2, bottom)

    }

    private fun getBottomRightCornerRoundedArc(bottom: Float, right: Float): RectF {
        return RectF(right - cornerRadius * 2, bottom - cornerRadius * 2, right, bottom)
    }

    private fun convertDpToPx(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }

}