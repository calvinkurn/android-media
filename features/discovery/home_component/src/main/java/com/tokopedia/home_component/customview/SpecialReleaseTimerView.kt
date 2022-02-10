package com.tokopedia.home_component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.tokopedia.home_component.R

class SpecialReleaseTimerView: FrameLayout {
    private var itemView: View?

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_custom_view_special_release_timer_view, this)
        this.itemView = view
    }

    fun setTimer() {

    }
}