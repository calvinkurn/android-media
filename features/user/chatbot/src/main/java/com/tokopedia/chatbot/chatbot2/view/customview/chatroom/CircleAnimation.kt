package com.tokopedia.chatbot.view.customview.chatroom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CircleAnimation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val START_ANGLE = 270f
        const val VALUE_ANIMATION_SWEEP_ANGLE_MAX = 360f
        const val VALUE_ANIMATION_SWEEP_ANGLE_START = 360f
        const val VALUE_ANIMATION_SWEEP_ANGLE_END = 0f

        const val STATE_DISABLED = "STATE_DISABLED"
        const val STATE_ENABLED = "STATE_ENABLED"
        const val STATE_LOADING = "STATE_LOADING"
    }

    private lateinit var drawAreaLoader: RectF
    private lateinit var drawAreaBackground: RectF
    private lateinit var paintGrey: Paint
    private lateinit var paintGreen: Paint
    private lateinit var valueAnimator: ValueAnimator

    var sweepAngle = 0f
    var state: String = STATE_ENABLED

    init {
        initAnimation()
    }

    private fun initAnimation() {
        drawAreaLoader = RectF()
        drawAreaBackground = RectF()

        paintGrey = Paint().apply {
            this.isAntiAlias = true
            this.color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN300)
        }
        paintGreen = Paint().apply {
            this.isAntiAlias = true
            this.color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
        }

        valueAnimator = ValueAnimator.ofFloat(VALUE_ANIMATION_SWEEP_ANGLE_START, VALUE_ANIMATION_SWEEP_ANGLE_END).apply {
            this.addUpdateListener { animation ->
                run {
                    sweepAngle = animation.animatedValue as Float
                    invalidate()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (state) {
            STATE_ENABLED -> {
                canvas?.drawArc(
                    drawAreaBackground,
                    START_ANGLE,
                    VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                    true,
                    paintGreen
                )
            }
            STATE_DISABLED -> {
                canvas?.drawArc(
                    drawAreaBackground,
                    START_ANGLE,
                    VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                    true,
                    paintGrey
                )
            }
            STATE_LOADING -> {
                canvas?.drawArc(
                    drawAreaBackground,
                    START_ANGLE,
                    VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                    true,
                    paintGreen
                )
                canvas?.drawArc(
                    drawAreaLoader,
                    START_ANGLE,
                    sweepAngle,
                    true,
                    paintGrey
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawAreaLoader.top = 0f
        drawAreaLoader.left = 0f
        drawAreaLoader.right = w.toFloat() - 0f
        drawAreaLoader.bottom = h.toFloat() - 0f

        drawAreaBackground.top = 0f
        drawAreaBackground.left = 0f
        drawAreaBackground.right = w.toFloat() - 0f
        drawAreaBackground.bottom = h.toFloat() - 0f
    }

    fun disable() {
        state = STATE_DISABLED
        invalidate()
    }

    fun enable() {
        state = STATE_ENABLED
        invalidate()
    }

    fun loading(durationInMillis: Long) {
        state = STATE_LOADING
        valueAnimator.apply {
            this.repeatCount = 0
            this.duration = durationInMillis
            this.start()
        }
        invalidate()
    }
}
