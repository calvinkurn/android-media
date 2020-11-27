package com.tokopedia.floatingwindow

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import com.tokopedia.floatingwindow.util.FloatingWindowHelper
import com.tokopedia.floatingwindow.util.registerDraggableTouchListener

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

    fun getViewByKey(key: String): View? {
        return viewMap[key]?.view
    }

    fun onCleared() {
        removeAllViews()
    }

    fun getWindowManager() = mWindowManager

    fun addView(key: String, view: View, layoutParams: WindowManager.LayoutParams, overwrite: Boolean) {
        if (viewMap.containsKey(key)) {
            if (overwrite) removeByKey(key)
            else return
        }

        view.registerDraggableTouchListener(
                initialPosition = { Point(layoutParams.x, layoutParams.y) },
                positionListener = { x, y -> setPosition(key, x, y) }
        )
        viewMap[key] = Property(view, layoutParams, Status.Queued)
        FloatingWindowHelper.startService(appContext)
    }

    fun removeByKey(key: String) {
        val prop = viewMap[key] ?: return

        mWindowManager.removeView(prop.view)
        viewMap.remove(key)
    }

    fun attachView() {
        viewMap.entries.forEach { entry ->
            val value = entry.value
            if (value.status == Status.Queued) {
                mWindowManager.addView(value.view, value.layoutParams)
                entry.setValue(value.copy(status = Status.Attached))
            }
        }
    }

    private fun removeAllViews() {
        viewMap
                .onEach { mWindowManager.removeView(it.value.view) }
                .clear()
    }

    private fun setPosition(key: String, x: Int, y: Int) {
        val currentLayoutParams = viewMap[key]?.layoutParams ?: return
        updateViewLayout(key, currentLayoutParams.apply {
            this.x = x
            this.y = y
        })
    }

    private fun updateViewLayout(key: String, layoutParams: WindowManager.LayoutParams) {
        try {
            val prop = viewMap[key] ?: return
            mWindowManager.updateViewLayout(prop.view, layoutParams)
            viewMap[key] = prop.copy(
                    layoutParams = layoutParams
            )
        } catch (e: Exception) {

        }
    }

    data class Property(
            val view: View,
            val layoutParams: WindowManager.LayoutParams,
            val status: Status
    )

    enum class Status {
        Queued,
        Attached
    }
}