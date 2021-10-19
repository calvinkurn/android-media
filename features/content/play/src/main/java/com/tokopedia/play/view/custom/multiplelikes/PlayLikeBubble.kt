package com.tokopedia.play.view.custom.multiplelikes

import android.graphics.*
import androidx.annotation.ColorInt

class Bubble(
    val icon: Bitmap,
    @ColorInt color: Int,
    private val speed: Int,
    reduceOpacity: Boolean,
    startXPos: Float,
    startYPos: Float,
) {
    private val startTime = System.currentTimeMillis()
    private val paint = Paint().apply {
        this.color = color
        this.alpha = if (reduceOpacity) 120 else 255
    }
    private val matrix = Matrix()

    private var xPos = startXPos
    private var yPos = startYPos

    private var scale = 0.01f

    private val circleRadius = icon.width.toFloat()
    private val circleCenter = circleRadius / 2

    private val leftMost: Float
        get() = xPos - circleRadius

    private val rightMost: Float
        get() = xPos + circleCenter

    private var direction = Direction.random()

    fun onTimeChanged(time: Long, width: Int, height: Int): Boolean {
        val timePassed = time - startTime
        val timePassedPercentage = timePassed.toFloat() / DURATION

        if (timePassedPercentage > 1) return true

        scale = when {
            timePassedPercentage < 0.2 -> {
                timePassedPercentage / 0.2f
            }
            timePassedPercentage > 0.8 -> {
                1 - (timePassedPercentage - 0.8f) / (1f - 0.8f)
            }
            else -> 1f
        }

        yPos = height - (timePassedPercentage * height)

        val prevXPos = xPos

        if (direction == Direction.Left) {
            if (leftMost + direction.baseMultiplier * speed < 0) direction = Direction.Right
        } else {
            if (rightMost + direction.baseMultiplier * speed > width) direction = Direction.Left
        }
        xPos = prevXPos + direction.baseMultiplier * speed

        return false
    }

    fun draw(
        canvas: Canvas,
    ) {
        matrix.reset()
        canvas.save()
        canvas.translate(xPos, yPos)
        canvas.scale(scale, scale)
        canvas.drawCircle(
            0f,
            0f,
            circleRadius,
            paint
        )
        canvas.drawBitmap(
            icon,
            -circleCenter,
            -circleCenter,
            paint
        )
        canvas.restore()
    }

    private enum class Direction(val baseMultiplier: Int) {
        Left(-1), Right(1);

        companion object {
            private val values = values()

            fun random(): Direction {
                return values.random()
            }
        }
    }

    companion object {
        private const val DURATION = 2500L
    }
}