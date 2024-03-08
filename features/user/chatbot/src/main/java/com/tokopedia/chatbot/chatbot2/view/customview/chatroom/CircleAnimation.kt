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

    private var drawAreaLoader: RectF = RectF()
    private var drawAreaBackground: RectF = RectF()
    private var paintGrey: Paint? = null
    private var paintGreen: Paint? = null
    private var valueAnimator: ValueAnimator? = null

    var sweepAngle = 0f
    var state: String = STATE_ENABLED

    init {
        initAnimation()
    }

    private fun initAnimation() {
        paintGrey = Paint().apply {
            this.isAntiAlias = true
            this.color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN300)
        }
        paintGreen = Paint().apply {
            this.isAntiAlias = true
            this.color = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
        }

        valueAnimator = ValueAnimator.ofFloat(
            VALUE_ANIMATION_SWEEP_ANGLE_START,
            VALUE_ANIMATION_SWEEP_ANGLE_END
        ).apply {
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
                drawStateEnabled(canvas)
            }

            STATE_DISABLED -> {
                drawStateDisabled(canvas)
            }

            STATE_LOADING -> {
                drawStateLoading(canvas)
            }
        }
    }

    private fun drawStateLoading(canvas: Canvas?) {
        paintGreen?.let {
            canvas?.drawArc(
                drawAreaBackground,
                START_ANGLE,
                VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                true,
                it
            )
        }
        paintGrey?.let {
            canvas?.drawArc(
                drawAreaLoader,
                START_ANGLE,
                sweepAngle,
                true,
                it
            )
        }
    }

    private fun drawStateEnabled(canvas: Canvas?) {
        paintGreen?.let {
            canvas?.drawArc(
                drawAreaBackground,
                START_ANGLE,
                VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                true,
                it
            )
        }
    }

    private fun drawStateDisabled(canvas: Canvas?) {
        paintGreen?.let {
            canvas?.drawArc(
                drawAreaBackground,
                START_ANGLE,
                VALUE_ANIMATION_SWEEP_ANGLE_MAX,
                true,
                it
            )
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

    fun loading(durationInSec: Int) {
        state = STATE_LOADING
        valueAnimator?.apply {
            this.repeatCount = 0
            this.duration = (durationInSec * 1000).toLong()
            this.start()
        }
        invalidate()
    }
}
