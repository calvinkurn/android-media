package com.tokopedia.notifications.inApp.ketupat

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.atan2


open class MyGestureListener {


    fun getDirection(x1: Float, y1: Float?, x2: Float, y2: Float): Direction {
        val angle: Double = getAngle(x1, y1, x2, y2)
        Log.d("direction angle", "{$angle}")
        return Direction.fromAngleSliced(angle)
    }

    open fun getAngle(x1: Float, y1: Float?, x2: Float, y2: Float): Double {
        val rad = y1?.minus(y2)?.let { atan2(it.toDouble(), (x2 - x1).toDouble()) }?.plus(Math.PI)
        if (rad != null) {
            return (rad * 180 / Math.PI + 180) % 360
        }
        return 0.0
    }

    fun getSlicePercent(x1: Float, y1: Float?, x2: Float, y2: Float, maxX: Int, maxY: Int, direction: Direction): Boolean {
        val dx = abs(x1 - x2).toInt()
        var dy = 0
        var isSliced = false
        y1?.let {
            dy = abs(y1 - y2).toInt()
            isSliced = isSlicedInRange(x1, y1, x2, y2, maxX, maxY, direction)
        }
        return isSliced
    }

    private fun slicedInRange(x1: Float, y1: Float, x2: Float, y2: Float, direction: Direction): Boolean {
        if (Direction.inRange(x1, 0, 100)
            && Direction.inRange(x2, 0, 100)
            && Direction.inRange(y1, 30, 75)
            && Direction.inRange(y2, 30, 75)
            && direction != Direction.horizontol) {
            return true
        }
        return false
    }

    private fun isSlicedInRange(x1: Float, y1: Float, x2: Float, y2: Float, maxX: Int, maxY: Int, direction: Direction): Boolean {
        val percentageX1 = (x1.div(maxX)).times(100)
        val percentageX2 = (x2.div(maxX)).times(100)
        val percentageY1 = (y1.div(maxY)).times(100)
        val percentageY2 = (y2.div(maxY)).times(100)

        val dy = abs(y2 - y1)
        val dx = abs(x1 - x2)
        val percentageDy = (dy.div(maxY)).times(100)
        val percentageDx = (dx.div(maxX)).times(100)
        val isInRange = slicedInRange(percentageX1, percentageY1, percentageX2, percentageY2, direction)
        Log.d("Percentage->", "x1 = $percentageX1, y1 = $percentageY1, x2 = $percentageX2, y2 = $percentageY2")
        Log.d("Percentage->>>", "dx = $percentageDx, dy = $percentageDy")
        if (percentageDy > 15 || percentageDx > 15) {
            return if (isHorizontalCut(direction, percentageY1, percentageY2)) {
                true
            } else isInRange
        }
        return false
    }

    private fun isHorizontalCut(direction: Direction, percentageY1: Float, percentageY2: Float): Boolean {
        if (direction == Direction.horizontol
            && Direction.inRange(percentageY1, 45, 67)
            && Direction.inRange(percentageY2, 45, 67)) {
            return true
        }
        return false
    }

    enum class Direction {
        up, vertical, left, up_right, up_left, horizontol, down_right, down_left;

        companion object {

            fun fromAngleSliced(angle: Double): Direction {
                return if (inRange(angle, 35f, 75f)) {
                    up_right
                } else if (inRange(angle, 90f, 145f)) {
                    up_left
                } else if (inRange(angle, 210f, 254f)) {
                    down_right
                } else if (inRange(angle, 286f, 330f)) {
                    down_left
                } else if (inRange(angle, 0f, 35f) || inRange(angle, 145f, 210f) || inRange(angle, 330f, 360f)) {
                    horizontol
                } else if (inRange(angle, 75f, 105f) || inRange(angle, 255f, 285f)) {
                    vertical
                } else {
                    left
                }
            }

             fun inRange(angle: Double, init: Float, end: Float): Boolean {
                return angle >= init && angle < end
            }

            fun inRange(point: Float, init: Int, end: Int): Boolean {
                return point >= init && point < end
            }
        }
    }

}
