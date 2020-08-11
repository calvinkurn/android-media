package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.Slide

class SingleProductAttachmentContainer : ConstraintLayout {

    private val widthMultiplier = 0.83

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = MeasureSpec.getSize(widthMeasureSpec) * widthMultiplier
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }

    fun gravityRight() {
        setLayoutGravity(Gravity.END)
    }

    fun gravityLeft() {
        setLayoutGravity(Gravity.START)
    }

    private fun setLayoutGravity(@Slide.GravityFlag gravity: Int) {
        (layoutParams as LinearLayout.LayoutParams).apply {
            this.gravity = gravity
        }
    }
}