package com.tokopedia.home_component.widget.card.timer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.R
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion

/**
 * Originally from: [com.tokopedia.unifycomponents.timer.TimerTextView]
 */
class TimerTextView : View {

    var mText = ""
    var mFirstText = ""
    var mSecondText = ""
    var mTextPaint: TextPaint = TextPaint()
    var placeholderStaticLayout: StaticLayout? = null
    var firstStaticLayout: StaticLayout? = null
    var secondStaticLayout: StaticLayout? = null
    var newFirstStaticLayout: StaticLayout? = null
    var newSecondStaticLayout: StaticLayout? = null
    private var firstWidth = 0
    private var secondWidth = 0
    private var mHeight: Int = 0
        set (value) {
            field = value
            newFirstPosY = mHeight * -1
            newSecondPosY = mHeight * -1
        }
    private var textColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_Static_White)
    private var textSize = resources.getDimensionPixelSize(R.dimen.count_down_timer_timertextview_text_size).toFloat()
    private var typeface = Typography.getFontType(context, true, Typography.DISPLAY_3)

    var firstPosY = 0
    var secondPosY = 0
    var newFirstPosY = 0
    var newSecondPosY = 0

    private val timerSingleTextSize = resources.getDimensionPixelSize(R.dimen.count_down_timer_timertextview_text_size).toFloat()
    private val timerHighlightTextSize = resources.getDimensionPixelSize(R.dimen.count_down_timer_timertextview_highlight_text_size).toFloat()

    private var shouldHandleSecondChar = true

    constructor(context: Context) : super(context) {
        initText()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initText()
    }

    fun setTextColor(color: Int) {
        textColor = color
        initText()
        requestLayout()
    }

    fun setType(type: Int) {
        typeface = Typography.getFontType(context, true, type)
        textSize = if(type == Typography.BODY_3) timerSingleTextSize else timerHighlightTextSize

        initText()
        requestLayout()
    }

    fun setText(text: String) {
        val prevText = mText
        mFirstText = safeExtractChar { text[0].toString() }
        mSecondText = safeExtractChar { text[1].toString() }

        shouldHandleSecondChar = mSecondText.isNotEmpty()

        mText = text

        if (prevText == "") {
            initText()
            requestLayout()
        } else {
            updateText(
                safeExtractChar { prevText[0].toString() },
                safeExtractChar { prevText[1].toString() }
            )
        }
    }

    private fun safeExtractChar(invoke: () -> String): String {
        return try {
            invoke()
        } catch (_: Throwable) {
            ""
        }
    }

    private fun initText() {
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = textSize
        mTextPaint.color = textColor
        mTextPaint.typeface = typeface

        /**
         * measure the width for 2 character
         * (using 4 since it is the widest char on numeric scale)
         */
        val width = mTextPaint.measureText(if (shouldHandleSecondChar) "44" else "4").toInt()
        placeholderStaticLayout = buildStaticLayout(mText, mTextPaint, width)

        firstWidth = mTextPaint.measureText(mFirstText).toInt()
        firstStaticLayout = buildStaticLayout(mFirstText, mTextPaint, firstWidth)

        if (shouldHandleSecondChar) {
            secondWidth = mTextPaint.measureText(mSecondText).toInt()
            secondStaticLayout = buildStaticLayout(mSecondText, mTextPaint, secondWidth)
        }
    }

    private fun buildStaticLayout(text: String, paint: TextPaint, width: Int): StaticLayout {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(1f, 0f)
                .setIncludePad(false)
                .build()
        } else {
            StaticLayout(
                text, paint,
                width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false
            )
        }
    }

    private fun updateText(prevFirstText: String, prevSecondText: String) {

        if (mSecondText != prevSecondText && mSecondText.isNotEmpty()) {
            newSecondStaticLayout = buildStaticLayout(mSecondText, mTextPaint, secondWidth)

            val newValueAnimator = ValueAnimator.ofInt(newSecondPosY, 0)
            newValueAnimator.addUpdateListener {
                newSecondPosY = it.animatedValue as Int

                invalidate()
            }

            newValueAnimator.duration = UnifyMotion.T4
            newValueAnimator.interpolator = UnifyMotion.EASE_IN_OUT
            newValueAnimator.start()

            val prevValueAnimator = ValueAnimator.ofInt(secondPosY, mHeight)
            prevValueAnimator.addUpdateListener {
                secondPosY = it.animatedValue as Int

                invalidate()
            }

            prevValueAnimator.duration = UnifyMotion.T4
            prevValueAnimator.interpolator = UnifyMotion.EASE_IN_OUT
            prevValueAnimator.start()
            prevValueAnimator.addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    secondStaticLayout = buildStaticLayout(mSecondText, mTextPaint, secondWidth)

                    secondPosY = 0
                    newSecondPosY = mHeight * -1

                    invalidate()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationStart(p0: Animator) {

                }

            })
        }

        if (mFirstText != prevFirstText && mFirstText.isNotEmpty()) {
            newFirstStaticLayout = buildStaticLayout(mFirstText, mTextPaint, firstWidth)

            val newValueAnimator = ValueAnimator.ofInt(newFirstPosY, 0)
            newValueAnimator.addUpdateListener {
                newFirstPosY = it.animatedValue as Int

                invalidate()
            }

            newValueAnimator.duration = UnifyMotion.T4
            newValueAnimator.interpolator = UnifyMotion.EASE_IN_OUT
            newValueAnimator.start()

            val prevValueAnimator = ValueAnimator.ofInt(firstPosY, mHeight)
            prevValueAnimator.addUpdateListener {
                firstPosY = it.animatedValue as Int

                invalidate()
            }

            prevValueAnimator.duration = UnifyMotion.T4
            prevValueAnimator.interpolator = UnifyMotion.EASE_IN_OUT
            prevValueAnimator.start()
            prevValueAnimator.addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    firstStaticLayout = buildStaticLayout(mFirstText, mTextPaint, firstWidth)

                    firstPosY = 0
                    newFirstPosY = mHeight * -1

                    invalidate()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationStart(p0: Animator) {

                }

            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(0f, firstPosY.toFloat())
        firstStaticLayout?.draw(canvas)
        canvas.restore()


        if (shouldHandleSecondChar) {
            canvas.save()
            canvas.translate(0f + width / 2, secondPosY.toFloat())
            secondStaticLayout?.draw(canvas)
            canvas.restore()
        }

        canvas.save()
        canvas.translate(0f, newFirstPosY.toFloat())
        newFirstStaticLayout?.draw(canvas)
        canvas.restore()

        if (shouldHandleSecondChar) {
            canvas.save()
            canvas.translate(0f + width / 2, newSecondPosY.toFloat())
            newSecondStaticLayout?.draw(canvas)
            canvas.restore()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Tell the parent layout how big this view would like to be
        // but still respect any requirements (measure specs) that are passed down.

        // determine the width
        var width: Int
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthRequirement = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement
        } else {
            width = placeholderStaticLayout!!.width + paddingLeft + paddingRight
            if (widthMode == MeasureSpec.AT_MOST) {
                if (width > widthRequirement) {
                    width = widthRequirement
                    // too long for a single line so relayout as multiline
                    placeholderStaticLayout = StaticLayout(
                        mText,
                        mTextPaint,
                        width,
                        Layout.Alignment.ALIGN_NORMAL,
                        1.0f,
                        0f,
                        false
                    )
                }
            }
        }

        // determine the height
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightRequirement = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightRequirement
        } else {
            mHeight = placeholderStaticLayout!!.height + paddingTop + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightRequirement)
            }
        }

        // Required call: set width and height
        setMeasuredDimension(width, mHeight)
    }
}
