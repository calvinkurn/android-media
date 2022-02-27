package com.tokopedia.catalog.model.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.jvm.JvmOverloads
import kotlin.math.abs

/***
 This class makes the recycler view scroll in the axis direction of the orientation set
 and sends the other axis scroll events to parent view.
 ***/
class OrientationAwareRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var lastX = 0.0f
    private var lastY = 0.0f
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val lm = layoutManager ?: return super.onInterceptTouchEvent(e)
        val allowScroll: Boolean
        val isHorizontalOrientation = lm.canScrollHorizontally()
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = e.x
                lastY = e.y
            }
            MotionEvent.ACTION_CANCEL -> {
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                // We're moving, so check if we're trying
                // to scroll vertically or horizontally so we don't intercept the wrong event.
                val currentX = e.x
                val currentY = e.y
                val dx = abs(currentX - lastX)
                val dy = abs(currentY - lastY)
                allowScroll = if (isHorizontalOrientation) {
                    dy < dx
                } else {
                    dy > dx
                }
                return allowScroll
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}