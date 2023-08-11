package com.tokopedia.stories.common

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.stories.common.animation.LinearEasingInterpolator

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

    private val circleBorderPath = Path()
    private val ovalRect = RectF()

    private val gradientStoriesBorderWidth = 2.dpToPx(resources.displayMetrics)
    private val seenStoriesBorderWidth = 1.dpToPx(resources.displayMetrics)

    private var mStoriesStatus = StoriesStatus.HasUnseenStories

    init {
        setWillNotDraw(false)
        seenStoriesPaint.color = Color.parseColor("#FFD6DFEB")
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

//        canvas.save()
//        canvas.clipStoriesBorderPath()
//        canvas.drawStoriesBorder()
//        canvas.restore()
//
//        super.onDraw(canvas)
    }

    fun startAnimation() {
        val animator = getAnimator()
        animator.start()
    }

    private fun setupBorderPath(width: Int, height: Int) {
        circleBorderPath.reset()
        circleBorderPath.addCircle(
            width / 2f,
            height / 2f,
            width / 2f - getBorderWidth(),
            Path.Direction.CW
        )
    }

    private fun getBorderWidth(): Int {
        return when (mStoriesStatus) {
            StoriesStatus.AllStoriesSeen -> seenStoriesBorderWidth
            StoriesStatus.HasUnseenStories -> gradientStoriesBorderWidth
            else -> 0
        }
    }

    private fun setupShader(width: Int, height: Int) {
        val shader = LinearGradient(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            Color.parseColor("#FF69F2E2"),
            Color.parseColor("#FF00AA5B"),
            Shader.TileMode.CLAMP
        )
        gradientStoriesPaint.shader = shader
    }

    private fun Canvas.clipStoriesBorderPath() {
        setupBorderPath(width, height)
        clipOutPathCompat(circleBorderPath)
    }

    private fun Canvas.drawStoriesBorder() {
        drawCircle(
            width / 2f,
            height / 2f,
            width / 2f,
            getStoriesBorderPaint()
        )
    }

    private fun getStoriesBorderPaint(): Paint {
        return when (mStoriesStatus) {
            StoriesStatus.HasUnseenStories -> gradientStoriesPaint
            else -> seenStoriesPaint
        }
    }

    private fun Canvas.clipOutPathCompat(path: Path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            clipOutPath(path)
        } else {
            clipPath(path, Region.Op.DIFFERENCE)
        }
    }

    private fun getAnimator(): Animator {
        val scaleDownValue = 0.94f
        val scaleOriginalValue = 0.97f
        val scaleUpValue = 1f

        val opaqueValue = 255
        val alphaValue = 0

        val scaleDown = ValueAnimator.ofFloat(scaleX, scaleDownValue)
            .setDuration(200)
        scaleDown.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        val scaleOriginal1 = ValueAnimator.ofFloat(scaleDownValue, scaleOriginalValue)
            .setDuration(200)
        scaleOriginal1.addUpdateListener {
            val scaleValue = it.animatedValue as Float
            scaleX = scaleValue
            scaleY = scaleValue
        }

        val alphaDashed = ValueAnimator.ofInt(alphaValue, opaqueValue)
            .setDuration(200)
        alphaDashed.addUpdateListener {
            arcPaint.alpha = it.animatedValue as Int
            postInvalidateOnAnimation()
        }

        val rotateSlow = ValueAnimator.ofFloat(0f, 160f)
            .setDuration(1200)
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
}
