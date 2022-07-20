package com.tokopedia.play.view.custom.multiplelikes

import android.graphics.*
import androidx.annotation.ColorInt

class Bubble(
    val icon: Bitmap,
    @ColorInt color: Int,
    reduceOpacity: Boolean,
    parentWidth: Int,
    parentHeight: Int,
) {
    private val startTime = System.currentTimeMillis()
    private val paint = Paint().apply {
        this.color = color
        this.alpha = if (reduceOpacity) HALF_OPACITY else FULL_OPACITY
    }
    private val matrix = Matrix()

    private val circleRadius = icon.width.toFloat()
    private val circleCenter = circleRadius / 2

    private var xPos: Float = (icon.width..(parentWidth - icon.width).coerceAtLeast(icon.width))
        .random().toFloat()
    private var yPos: Float = parentHeight.toFloat()

    private val bouncingLimit = (LOWER_LIMIT_BOUNCING_DISTANCE..UPPER_LIMIT_BOUNCING_DISTANCE).random()
    private val bouncingMultiplier = (LOWER_BOUNCING_MULTIPLIER_X..UPPER_BOUNCING_MULTIPLIER_X).random()

    private val xStart = xPos - bouncingLimit
    private val xEnd = xPos + bouncingLimit

    private var scale = INITIAL_SCALE

    private var direction = listOf(Direction.Left, Direction.Right).random()

    fun onTimeChanged(time: Long, width: Int, height: Int): Boolean {
        val timePassed = time - startTime
        val timePassedPercentage = timePassed.toFloat() / DURATION

        if(yPos <= 0) return true

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

        when(val dir = direction) {
            is Direction.Center -> {
                if(dir.counter + 1 == dir.threshold) {
                    direction = dir.next
                }
                else dir.counter++
            }
            Direction.Right -> {
                xPos += bouncingMultiplier
                if(xPos > xEnd) {
                    direction = Direction.Center(Direction.Left, 0)
                }
            }
            Direction.Left -> {
                xPos -= bouncingMultiplier
                if(xPos < xStart) {
                    direction = Direction.Center(Direction.Right, 0)
                }
            }
        }

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

    private sealed class Direction {
        val threshold = 5

        object Right: Direction()
        object Left: Direction()
        data class Center(val next: Direction, var counter: Int = 0): Direction()
    }

    companion object {
        private const val DURATION = 2500L

        private const val LOWER_LIMIT_BOUNCING_DISTANCE = 10
        private const val UPPER_LIMIT_BOUNCING_DISTANCE = 40
        private const val LOWER_BOUNCING_MULTIPLIER_X = 3
        private const val UPPER_BOUNCING_MULTIPLIER_X = 8

        private const val INITIAL_SCALE = 0.01f

        private const val HALF_OPACITY = 120
        private const val FULL_OPACITY = 255
    }
}