package com.tokopedia.stories.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Region
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created by kenny.hadisaputra on 11/08/23
 */
class StoriesAvatarImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val gradientStoriesPaint = Paint()
    private val seenStoriesPaint = Paint()
    private val circleImagePath = Path()
    private val circleBorderPath = Path()

    private var mStoriesStatus = StoriesStatus.NoStories

    private val gradientStoriesBorderWidth = 2.dpToPx(resources.displayMetrics)
    private val seenStoriesBorderWidth = 1.dpToPx(resources.displayMetrics)
    private val imageToBorderMargin = 4.dpToPx(resources.displayMetrics)

    init {
        seenStoriesPaint.color = Color.parseColor("#FFD6DFEB")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipStoriesBorderPath()
        canvas.drawStoriesBorder()
        canvas.restore()

        canvas.save()
        canvas.clipImagePath()
        super.onDraw(canvas)
        canvas.restore()
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

    private fun setupImagePath(width: Int, height: Int) {
        circleImagePath.reset()
        circleImagePath.addCircle(
            width / 2f,
            height / 2f,
            width / 2f - getImageMargin(),
            Path.Direction.CW
        )
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

    private fun Canvas.clipImagePath() {
        setupImagePath(width, height)
        clipPath(circleImagePath)
    }

    private fun getBorderWidth(): Int {
        return when (mStoriesStatus) {
            StoriesStatus.AllStoriesSeen -> seenStoriesBorderWidth
            StoriesStatus.HasUnseenStories -> gradientStoriesBorderWidth
            else -> 0
        }
    }

    private fun getImageMargin(): Int {
        return when (mStoriesStatus) {
            StoriesStatus.NoStories -> 0
            else -> imageToBorderMargin
        }
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
}
