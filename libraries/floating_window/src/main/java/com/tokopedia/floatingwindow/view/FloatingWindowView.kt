package com.tokopedia.floatingwindow.view

import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.tokopedia.floatingwindow.util.FloatingWindowLayouter
import com.tokopedia.floatingwindow.util.ScreenLayoutHelper

/**
 * Created by jegul on 27/11/20
 */
class FloatingWindowView private constructor(
        val key: String,
        val view: View,
        val layoutParams: WindowManager.LayoutParams
) {

    private val layouter = FloatingWindowLayouter(view, layoutParams)

    private var mOnDraggedListener: OnDraggedListener = OnDraggedListener.DEFAULT_MOVE

    fun doOnDragged(onDragged: OnDraggedListener) {
        mOnDraggedListener = onDragged
    }

    internal fun onDragged(point: Point) {
        mOnDraggedListener.onDragged(layouter, point)
    }

    class Builder(
            val key: String,
            val view: View,
            val width: Int,
            val height: Int
    ) {

        private val screenLayoutHelper = ScreenLayoutHelper()

        private var x = (screenLayoutHelper.widthPixels - width) / 2
        private var y = (screenLayoutHelper.heightPixels - height) / 2
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
    }

    fun interface OnDraggedListener {

        companion object {
            val DEFAULT_MOVE: OnDraggedListener
                get() = OnDraggedListener { layouter, point -> layouter.updatePosition(point.x, point.y) }

            val DEFAULT_STAY: OnDraggedListener
                get() = OnDraggedListener { _, _ -> }
        }

        fun onDragged(layouter: FloatingWindowLayouter, point: Point)
    }

    fun interface OnClickedListener {

        fun onClicked(layouter: FloatingWindowLayouter)
    }
}