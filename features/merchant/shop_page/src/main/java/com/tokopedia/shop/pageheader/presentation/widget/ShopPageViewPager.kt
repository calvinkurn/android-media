package com.tokopedia.shop.pageheader.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by nathan on 3/10/18.
 */
class ShopPageViewPager : TouchViewPager {
    var pagingStatus = true
        private set
    private var timer = Timer()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return pagingStatus && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        try {
            return pagingStatus && super.onInterceptTouchEvent(event)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
        return false
    }

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return pagingStatus && super.canScroll(v, checkV, dx, x, y)
    }

    fun setPagingEnabled(pagingEnabled: Boolean) {
        if (pagingStatus == pagingEnabled) {
            return
        }
        if (pagingStatus) {
            timer.cancel()
            pagingStatus = false
            return
        }
        // Update with timer
        updatePagingStateWithTimer(true)
    }

    private fun updatePagingStateWithTimer(pagingEnabled: Boolean) {
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                pagingStatus = pagingEnabled
            }
        }, DELAY_STATE_CHANGED)
    }

    override fun removeView(view: View) {
        super.removeView(view)
    }

    companion object {
        private val DELAY_STATE_CHANGED = TimeUnit.SECONDS.toMillis(2)
    }
}