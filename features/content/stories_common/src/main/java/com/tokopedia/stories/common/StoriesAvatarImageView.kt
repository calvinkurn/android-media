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

    private val gradientPaint = Paint()
    private val circleImagePath = Path()
    private val circleBorderPath = Path()

    private val borderWidth = 2.dpToPx(resources.displayMetrics)
    private val imageToBorderMargin = 4.dpToPx(resources.displayMetrics)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupImagePath(w, h)
        setupBorderPath(w, h)
        setupShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipOutPathCompat(circleBorderPath)
        drawBackground(canvas)
        canvas.restore()

        canvas.save()
        canvas.clipPath(circleImagePath)
        super.onDraw(canvas)
        canvas.restore()
    }

    private fun setupBorderPath(width: Int, height: Int) {
        circleBorderPath.reset()
        circleBorderPath.addCircle(
            width / 2f,
            height / 2f,
            width / 2f - borderWidth,
            Path.Direction.CW
        )
    }

    private fun setupImagePath(width: Int, height: Int) {
        circleImagePath.reset()
        circleImagePath.addCircle(
            width / 2f,
            height / 2f,
            width / 2f - imageToBorderMargin,
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
        gradientPaint.shader = shader
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawCircle(
            width / 2f,
            height / 2f,
            width / 2f,
            gradientPaint
        )
    }

    private fun Canvas.clipOutPathCompat(path: Path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            clipOutPath(path);
        } else {
            clipPath(path, Region.Op.DIFFERENCE)
        }
    }
}
