package com.tokopedia.floatingwindow.view

import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.tokopedia.floatingwindow.util.FloatingWindowLayouter

/**
 * Created by jegul on 27/11/20
 */
class FloatingWindowView private constructor(
        val key: String,
        val view: View,
        val layoutParams: WindowManager.LayoutParams
) {

    val DRAG_MOVE: (FloatingWindowLayouter, Point) -> Unit = { layouter, point -> layouter.updatePosition(point.x, point.y) }
    val DRAG_STAY: (FloatingWindowLayouter, Point) -> Unit = { _, _ ->  }

    private val layouter = FloatingWindowLayouter(view, layoutParams)

    private var mOnClick: (FloatingWindowLayouter) -> Unit = {}
    private var mOnDragged: (FloatingWindowLayouter, Point) -> Unit = DRAG_MOVE

    fun doOnClick(onClick: (layouter: FloatingWindowLayouter) -> Unit) {
        mOnClick = onClick
    }

    fun doOnDragged(onDragged: (layouter: FloatingWindowLayouter, point: Point) -> Unit) {
        mOnDragged = onDragged
    }

    internal fun onClick() {
        mOnClick(layouter)
    }

    internal fun onDragged(point: Point) {
        mOnDragged(layouter, point)
    }

    class Builder(
            val key: String,
            val view: View,
            val width: Int,
            val height: Int
    ) {

        private val displayMetrics = getCurrentDisplayMetrics()

        private var x = (displayMetrics.widthPixels - width) / 2
        private var y = (displayMetrics.heightPixels - height) / 2
        private var mGravity: Int = getWindowManagerDefaultGravity()

        fun setX(x: Int): Builder {
            this.x = x
            return this
        }

        fun setY(y: Int): Builder {
            this.y = y
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.mGravity = gravity
            return this
        }

        fun build(): FloatingWindowView {
            return FloatingWindowView(
                    key = key,
                    view = view,
                    layoutParams = createWindowManagerLayoutParams()
            )
        }

        private fun createWindowManagerLayoutParams(): WindowManager.LayoutParams {
            return WindowManager.LayoutParams(
                    width,
                    height,
                    x,
                    y,
                    getWindowManagerLayoutParamsType(),
                    getWindowManagerFlags(),
                    PixelFormat.TRANSLUCENT
            ).apply {
                gravity = mGravity
            }
        }

        private fun getWindowManagerLayoutParamsType(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }

        private fun getWindowManagerFlags(): Int {
            return WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        }

        private fun getWindowManagerDefaultGravity(): Int {
            return Gravity.TOP or Gravity.START
        }

        private fun getCurrentDisplayMetrics(): DisplayMetrics {
            return Resources.getSystem().displayMetrics
        }
    }
}