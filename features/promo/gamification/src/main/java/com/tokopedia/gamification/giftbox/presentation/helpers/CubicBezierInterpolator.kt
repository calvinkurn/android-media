package com.tokopedia.gamification.giftbox.presentation.helpers

import android.graphics.PointF
import android.view.animation.Interpolator
import kotlin.math.abs

class CubicBezierInterpolator : Interpolator {

    private var start: PointF = PointF()
    private var end: PointF = PointF()
    private var a = PointF()
    private var b = PointF()
    private var c = PointF()

    companion object{
        private const val BREAKING_CONSTANT=1e-3
    }

    constructor(start:PointF,end:PointF){
        if(start.x < 0 || start.x>1){
            throw IllegalArgumentException("startX value must be in the range [0, 1]")
        }
        if(end.x < 0 || end.x>1){
            throw IllegalArgumentException("endX value must be in the range [0, 1]")
        }
        this.start = start
        this.end = end
    }

    constructor(startX: Float, startY: Float, endX: Float, endY: Float) : this(PointF(startX, startY), PointF(endX, endY))

    constructor(startX: Double, startY: Double, endX: Double, endY: Double) : this(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())

    override fun getInterpolation(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    private fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * start.y
        b.y = 3 * (end.y - start.y) - c.y
        a.y = 1 - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    private fun getXForTime(time: Float): Float {
        var x = time
        var z: Float
        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (abs(z) < BREAKING_CONSTANT) {
                break
            }
            x -= z / getXDerivate(x)
        }
        return x
    }
    private fun getXDerivate(t: Float): Float {
        return c.x + t * (2 * b.x + 3 * a.x * t)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * start.x
        b.x = 3 * (end.x - start.x) - c.x
        a.x = 1 - c.x - b.x
        return time * (c.x + time * (b.x + time * a.x))
    }
}