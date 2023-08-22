package com.tokopedia.stories.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.addListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.stories.widget.animation.LinearEasingInterpolator
import kotlin.math.min

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
internal class StoriesBorderView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val gradientStoriesPaint = Paint()
    private val seenStoriesPaint = Paint()
    private val arcPaint = Paint().apply {
        color = Color.GRAY
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        alpha = 0
    }
    private val clipCenterPaint = Paint().apply {
        color = Color.GRAY
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private val ovalRect = RectF()

    private var mBorderConfig = BorderConfiguration(
        unseenStoriesWidth = BorderValue.Adaptive,
        seenStoriesWidth = BorderValue.Adaptive
    )
        set(value) {
            field = value
            invalidate()
        }

    private var mStoriesStatus = StoriesStatus.NoStories

    private var mAnimator: Animator? = null

    init {
        setWillNotDraw(false)
        seenStoriesPaint.color = Color.parseColor("#80BFC9D9")

        setOriginalTransformationValue()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupShader(w, h)
        ovalRect.set(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val checkPoint = canvas.saveLayer(null, null)

        canvas.drawCircle(
            width / 2f,
            height / 2f,
            width / 2f,
            getStoriesBorderPaint()
        )

        repeat(4) {
            canvas.drawArc(
                ovalRect,
                20f + it * 90f,
                15f,
                true,
                arcPaint
            )

            canvas.drawArc(
                ovalRect,
                75f + it * 90f,
                15f,
                true,
                arcPaint
            )
        }

        canvas.drawCircle(
            width / 2f,
            height / 2f,
            width / 2f - getBorderWidth(),
            clipCenterPaint
        )

        canvas.restoreToCount(checkPoint)
    }

    internal fun startAnimation() {
        endAnimation()
        mAnimator = getAnimator().apply {
            addListener(
                onEnd = {
                    setOriginalTransformationValue()
                    invalidate()
                }
            )
        }
        mAnimator?.start()
    }

    internal fun endAnimation() {
        mAnimator?.cancel()
    }

    internal fun setBorderConfig(onNewConfig: (BorderConfiguration) -> BorderConfiguration) {
        mBorderConfig = onNewConfig(mBorderConfig)
    }

    internal fun setStoriesStatus(status: StoriesStatus) {
        mStoriesStatus = status
        invalidate()
    }

    private fun setOriginalTransformationValue() {
        scaleX = 0.97f
        scaleY = 0.97f
        rotation = 0f

        arcPaint.alpha = 0
    }

    private fun getBorderWidth(): Float {
        return when (mStoriesStatus) {
            StoriesStatus.AllStoriesSeen -> getStoriesSeenBorderValue(mBorderConfig.seenStoriesWidth)
            StoriesStatus.HasUnseenStories -> getUnseenStoriesBorderValue(mBorderConfig.unseenStoriesWidth)
            else -> 0f
        }
    }

    private fun getStoriesSeenBorderValue(borderValue: BorderValue): Float {
        return when (borderValue) {
            BorderValue.Adaptive -> getStoriesSeenAdaptiveBorderWidth()
            is BorderValue.Fixed -> borderValue.value
        }
    }

    private fun getStoriesSeenAdaptiveBorderWidth(): Float {
        val minSize = min(width, height)
        val sizeInDp = minSize.pxToDp(resources.displayMetrics)

        val borderInPx = when {
            sizeInDp >= 64 -> 1f
            sizeInDp < 32 -> 0.5f
            else -> 0.5f
        }

        return borderInPx.dpToPx()
    }

    private fun getUnseenStoriesBorderValue(borderValue: BorderValue): Float {
        return when (borderValue) {
            BorderValue.Adaptive -> getUnseenStoriesAdaptiveBorderWidth()
            is BorderValue.Fixed -> borderValue.value
        }
    }

    private fun getUnseenStoriesAdaptiveBorderWidth(): Float {
        val minSize = min(width, height)
        val sizeInDp = minSize.pxToDp(resources.displayMetrics)

        Log.d("StoriesBorderView", "Size in dp: $sizeInDp")
        val borderInPx = when {
            sizeInDp >= 64 -> 2f
            sizeInDp < 32 -> 1f
            else -> 1.5f
        }

        return borderInPx.dpToPx()
    }

    private fun setupShader(width: Int, height: Int) {
        val shader = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            Color.parseColor("#FF83ECB2"),
            Color.parseColor("#FF00AA5B"),
            Shader.TileMode.CLAMP
        )
        gradientStoriesPaint.shader = shader
    }

    private fun getStoriesBorderPaint(): Paint {
        return when (mStoriesStatus) {
            StoriesStatus.HasUnseenStories -> gradientStoriesPaint
            else -> seenStoriesPaint
        }
    }

    private fun getAnimator(): Animator {
        val scaleDownValue = 0.94f
        val scaleOriginalValue = 0.97f
        val scaleUpValue = 1f

        val opaqueValue = 255
        val alphaValue = 0

        val scaleDown = ValueAnimator.ofFloat(scaleX, scaleDownValue)
            .setDuration(400)
        scaleDown.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        val scaleOriginal1 = ValueAnimator.ofFloat(scaleDownValue, scaleOriginalValue)
            .setDuration(400)
        scaleOriginal1.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        val alphaDashed = ValueAnimator.ofInt(alphaValue, opaqueValue)
            .setDuration(400)
        alphaDashed.addUpdateListener {
            arcPaint.alpha = it.animatedValue as Int
            postInvalidateOnAnimation()
        }

        val rotateSlow = ValueAnimator.ofFloat(0f, 160f)
            .setDuration(1600)
        rotateSlow.interpolator = LinearEasingInterpolator()
        rotateSlow.addUpdateListener {
            val rotationValue = it.animatedValue as Float
            rotation = rotationValue
        }

        val rotateFast = ValueAnimator.ofFloat(160f, 360f)
            .setDuration(600)
        rotateFast.interpolator = FastOutLinearInInterpolator()
        rotateFast.addUpdateListener {
            val rotationValue = it.animatedValue as Float
            rotation = rotationValue
        }

        val opaqueDashed = ValueAnimator.ofInt(opaqueValue, alphaValue)
            .setDuration(400)
        opaqueDashed.addUpdateListener {
            arcPaint.alpha = it.animatedValue as Int
            postInvalidateOnAnimation()
        }

        val scaleUpOver = ValueAnimator.ofFloat(scaleOriginalValue, scaleUpValue)
            .setDuration(400)
        scaleUpOver.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        val scaleOriginal2 = ValueAnimator.ofFloat(scaleUpValue, scaleOriginalValue)
            .setDuration(400)
        scaleOriginal2.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        return AnimatorSet().apply {
            play(scaleDown).before(scaleOriginal1)
            play(scaleOriginal1).with(alphaDashed)
            play(rotateSlow).after(scaleOriginal1)
            play(opaqueDashed).after(rotateSlow)
            play(opaqueDashed).with(rotateFast).with(scaleUpOver)
            play(scaleOriginal2).after(scaleUpOver)
        }
    }

    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density

    internal data class BorderConfiguration(
        val unseenStoriesWidth: BorderValue,
        val seenStoriesWidth: BorderValue
    )

    internal sealed interface BorderValue {
        object Adaptive : BorderValue

        @JvmInline
        value class Fixed(val value: Float) : BorderValue {
            constructor(value: Int) : this(value.toFloat())
        }
    }
}
