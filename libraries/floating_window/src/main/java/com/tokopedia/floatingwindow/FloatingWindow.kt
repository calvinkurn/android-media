package com.tokopedia.floatingwindow

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import com.tokopedia.floatingwindow.util.FloatingWindowHelper
import com.tokopedia.floatingwindow.util.registerDraggableTouchListener
import com.tokopedia.floatingwindow.view.FloatingWindowView

/**
 * Created by jegul on 26/11/20
 */
internal class FloatingWindow private constructor(context: Context) {

    companion object {

        private var INSTANCE: FloatingWindow? = null

        fun getInstance(context: Context): FloatingWindow = synchronized(this) {
            if (INSTANCE == null) {
                INSTANCE = FloatingWindow(context)
            }
            return INSTANCE!!
        }

        fun clearInstance() = synchronized(this) {
            if (INSTANCE != null) {
                INSTANCE!!.onCleared()

                INSTANCE = null
            }
        }
    }

    private val viewMap = mutableMapOf<String, Property>()

    private val appContext = context.applicationContext

    private val mWindowManager = appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun getViewByKey(key: String): FloatingWindowView? {
        return viewMap[key]?.floatingView
    }

    fun getWindowManager() = mWindowManager

    fun addView(key: String, floatingView: FloatingWindowView, overwrite: Boolean) {
        if (viewMap.containsKey(key)) {
            if (overwrite) removeByKey(key)
            else return
        }

        floatingView.view.registerDraggableTouchListener(
                initialPosition = { Point(floatingView.layoutParams.x, floatingView.layoutParams.y) },
                onDragged = { x, y ->
                    floatingView.onDragged(Point(x, y))
                }
        )
        viewMap[key] = Property(floatingView, Status.Queued)
        FloatingWindowHelper.startService(appContext)
    }

    fun removeByKey(key: String) = synchronized(this) {
        val prop = viewMap[key] ?: return

        if (prop.status == Status.Attached) mWindowManager.removeView(prop.floatingView.view)
        viewMap.remove(key)

        if (viewMap.isEmpty()) FloatingWindowHelper.stopService(appContext)
    }

    fun attachView() {
        viewMap.entries.forEach { entry ->
            val value = entry.value
            if (value.status == Status.Queued) {
                mWindowManager.addView(value.floatingView.view, value.floatingView.layoutParams)
                entry.setValue(value.copy(status = Status.Attached))
            }
        }
    }

    fun removeAllViews() {
        viewMap
                .onEach { removeByKey(it.key) }
                .clear()
    }

    fun updateViewLayout(key: String, layoutParams: WindowManager.LayoutParams) {
        try {
            val prop = viewMap[key] ?: return
            mWindowManager.updateViewLayout(prop.floatingView.view, layoutParams)
        } catch (e: Exception) {
            //ignored
        }
    }

    fun updateViewLayout(view: View, layoutParams: WindowManager.LayoutParams) {
        try {
            mWindowManager.updateViewLayout(view, layoutParams)
        } catch (e: Exception) {
            //ignored
        }
    }

    private fun onCleared() {
        removeAllViews()
    }

    data class Property(
            val floatingView: FloatingWindowView,
            val status: Status
    )

    enum class Status {
        Queued,
        Attached
    }
}