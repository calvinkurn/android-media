package com.tokopedia.play.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

/**
 * Created by jegul on 29/04/20
 */
class ScaleFriendlyFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    val isScaling: Boolean
        get() = scaleX != FULL_SCALE && scaleY != FULL_SCALE

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isScaling) true
        else super.onInterceptTouchEvent(ev)
    }

    companion object {
        private const val FULL_SCALE = 1.0f
    }
}