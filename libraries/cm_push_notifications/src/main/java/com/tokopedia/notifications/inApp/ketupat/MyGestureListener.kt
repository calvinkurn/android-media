package com.tokopedia.notifications.inApp.ketupat

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.atan2


open class MyGestureListener {

    private var percentageDx = 0.0f
    private var percentageDy = 0.0f

    fun getDirection(x1: Float, y1: Float?, x2: Float, y2: Float): Direction {
        val angle: Double = getAngle(x1, y1, x2, y2)
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
        var dy = 0
        var isSliced = false
        y1?.let {
            dy = abs(it - y2).toInt()
            isSliced = isSlicedInRange(x1, y1, x2, y2, maxX, maxY, direction)
        }
        return isSliced
    }

    private fun slicedInRange(x1: Float, y1: Float, x2: Float, y2: Float, direction: Direction): Boolean {
        if (Direction.inRange(x1, 0, 100)
            && Direction.inRange(x2, 0, 100)
            && Direction.inRange(y1, 5, 45)
            && Direction.inRange(y2, 5, 45)
            && direction != Direction.horizontol
            && direction != Direction.vertical) {
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
        percentageDy = (dy.div(maxY)).times(100)
        percentageDx = (dx.div(maxX)).times(100)
        val isInRange = slicedInRange(percentageX1, percentageY1, percentageX2, percentageY2, direction)
//        Log.d("Percentage->", "x1 = $percentageX1, y1 = $percentageY1, x2 = $percentageX2, y2 = $percentageY2")
//        Log.d("Percentage->>>", "dx = $percentageDx, dy = $percentageDy")
        if (percentageDy > 13 || percentageDx > 13) {
            return if (isHorizontalCut(direction, percentageY1, percentageY2)) {
                true
            } else if (isVerticalCut(direction, percentageX1, percentageX2))  {
                true
            }
            else isInRange
        }
        return false
    }

    fun getGesturePercent(): Pair<Float, Float> {
        return Pair(percentageDx, percentageDy)
    }

    private fun isHorizontalCut(direction: Direction, percentageY1: Float, percentageY2: Float): Boolean {
        if (direction == Direction.horizontol
            && Direction.inRange(percentageY1, 17, 35)
            && Direction.inRange(percentageY2, 17, 35)) {
            return true
        }
        return false
    }

    private fun isVerticalCut(direction: Direction, percentageX1: Float, percentageX2: Float): Boolean {
        if (direction == Direction.vertical
            && Direction.inRange(percentageX1, 30, 70)
            && Direction.inRange(percentageX2, 30, 70)) {
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
