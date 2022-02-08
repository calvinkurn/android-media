package com.tokopedia.media.preview.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class PreviewViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        return false
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        return false
//    }

}