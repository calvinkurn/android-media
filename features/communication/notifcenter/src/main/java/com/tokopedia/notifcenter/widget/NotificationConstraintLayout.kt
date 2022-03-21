package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import androidx.constraintlayout.widget.ConstraintLayout

class NotificationConstraintLayout : ConstraintLayout {

    var onTouchUp: () -> Unit = {}

    constructor(context: Context?) : super(context)
    constructor(
            context: Context?, attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
            context: Context?, attrs: AttributeSet?,
            defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setOnTouchUpListener(upAction: () -> Unit) {
        this.onTouchUp = upAction
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            ACTION_UP -> onTouchUp.invoke()
        }
        return super.onInterceptTouchEvent(ev)
    }
}